package com.dynomo.ads_manager

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.ViewGroup
import com.dynomo.ads_manager.ads.admob.AdMobOpen
import com.dynomo.ads_manager.ads.admob.NativeAd
import com.dynomo.ads_manager.ads.admob.Util
import com.dynomo.ads_manager.ads.admob.Util.Companion.isEligibleToShowInterstitial
import com.dynomo.ads_manager.model.Ad
import com.dynomo.ads_manager.model.AdSize
import com.dynomo.ads_manager.model.AdType
import com.dynomo.ads_manager.model.AdsConfig
import com.dynomo.ads_manager.model.NativeAdSize
import com.dynomo.ads_manager.store.Store
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.NativeAdView

const val TAG = "com.dynomo.adsmanager"

class AdsManager {

    companion object {
        private var adMobOpenInstance: AdMobOpen? = null
        fun init(context: Context) {
            initGoogleAds(context)
            adMobOpenInstance = AdMobOpen()
        }

        fun setAdsConfig(adsConfig: AdsConfig, interstitialInterval: Int) {
            Store.adsConfig = adsConfig
            Store.interstitialIntervalInSecond = interstitialInterval
        }

        fun showBannerAd(activity: Activity, target: ViewGroup) {
            if (!Store.adsConfig.enableBanner) return
            Store.adsConfig.ads.forEach {
                if (it.type == AdType.AdMob) {
                    kotlin.runCatching {
                        val adView = AdView(activity)
                        val adRequest = AdRequest.Builder().build()
                        val adSize =
                            if (it.adSize == AdSize.MEDIUM) com.google.android.gms.ads.AdSize.MEDIUM_RECTANGLE
                            else Util.calculateBannerSize(activity, target)
                        adView.adUnitId = it.bannerID
                        adView.setAdSize(adSize)
                        target.removeAllViews()
                        target.addView(adView)
                        adView.loadAd(adRequest)
                    }.onSuccess { return }
                        .onFailure { err -> Log.d(TAG, "failed to load banner ad - " + err.message) }
                }
            }
        }

        fun showInterstitialAd(activity: Activity, onExit: () -> Unit) {
            if (!Store.adsConfig.enableInterstitial) {
                onExit()
                return
            }
            Store.adsConfig.ads.forEach {
                if (!isEligibleToShowInterstitial()) {
                    onExit()
                    return
                }
                if (it.type == AdType.AdMob) {
                    InterstitialAd.load(
                        activity,
                        it.interstitialID,
                        AdRequest.Builder().build(),
                        object : InterstitialAdLoadCallback() {
                            override fun onAdFailedToLoad(p0: LoadAdError) {
                                Log.e(TAG, "failed to load interstitial ad - " + p0.message)
                            }

                            override fun onAdLoaded(p0: InterstitialAd) {
                                Store.lastInterstitialShowTimeUnix = System.currentTimeMillis()
                                p0.fullScreenContentCallback =
                                    object : FullScreenContentCallback() {
                                        override fun onAdDismissedFullScreenContent() {
                                            onExit()
                                        }
                                    }
                                p0.show(activity)
                                return
                            }
                        })
                }
            }
        }

        fun showNativeAd(activity: Activity, target: ViewGroup) {
            if (!Store.adsConfig.enableNative) return
            Store.adsConfig.ads.forEach{
                if (it.type == AdType.AdMob) {
                    AdLoader.Builder(activity, it.nativeID)
                        .forNativeAd { ad ->
                            val layout = when(it.nativeAdSize) {
                                NativeAdSize.SMALL -> R.layout.admob_native_small
                                NativeAdSize.SMALL_RECTANGLE -> R.layout.admob_native_small_rect
                                else -> R.layout.admob_native_big
                            }

                            val view = activity.layoutInflater.inflate(layout, null) as NativeAdView
                            NativeAd.populateNativeAdView(ad, view)
                            target.removeAllViews()
                            target.addView(view)
                        }
                        .withAdListener(object: AdListener(){
                            override fun onAdFailedToLoad(p0: LoadAdError) {
                                super.onAdFailedToLoad(p0)
                                Log.d(TAG, "failed to load native ad - " + p0.message)
                            }

                            override fun onAdLoaded() {
                                super.onAdLoaded()
                                return
                            }
                        }).build()
                        .loadAd(AdRequest.Builder().build())
                }
            }
        }

        fun showOpenAd(activity: Activity, onComplete: () -> Unit) {
            if (!Store.adsConfig.enableOpen) {
                onComplete()
                return
            }
            Store.adsConfig.ads.forEach {
                when (it.type) {
                    AdType.AdMob -> adMobOpenInstance?.showAdIfAvailable(activity, it.openID, onComplete)
                    else -> {
                        onComplete()
                        return
                    }
                }
                return
            }
        }

        fun isOpenAdShowing(): Boolean {
            Store.adsConfig.ads.forEach { it ->
                when (it.type) {
                    AdType.AdMob -> adMobOpenInstance?.let { instance -> if (instance.isShowingAd) return true }
                    else -> {}
                }
            }

            return false
        }

        private fun initGoogleAds(context: Context) {
            MobileAds.initialize(context) {}
        }
    }
}