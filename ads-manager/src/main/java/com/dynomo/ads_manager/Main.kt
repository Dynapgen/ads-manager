package com.dynomo.ads_manager

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.ViewGroup
import com.dynomo.ads_manager.model.Ad
import com.dynomo.ads_manager.model.AdSize
import com.dynomo.ads_manager.model.AdType
import com.dynomo.ads_manager.store.Store
import com.dynomo.ads_manager.util.AdMob
import com.dynomo.ads_manager.util.AdMob.Companion.isEligibleToShowInterstitial
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

const val TAG = "AdsManager"

class AdsManager {

    companion object {
        fun init(context: Context, ads: List<Ad>, interstitialInterval: Int) {
            Store.Ads = ads
            Store.interstitialIntervalInSecond = interstitialInterval
            if (ads.any { it.type == AdType.AdMob }) initGoogleAds(context)
        }

        fun showBannerAd(activity: Activity, target: ViewGroup) {
            Store.Ads.forEach {
                if (it.type == AdType.AdMob) {
                    kotlin.runCatching {
                        val adView = AdView(activity)
                        val adRequest = AdRequest.Builder().build()
                        val adSize =
                            if (it.adSize == AdSize.MEDIUM) com.google.android.gms.ads.AdSize.MEDIUM_RECTANGLE
                            else AdMob.calculateBannerSize(activity, target)
                        adView.adUnitId = it.bannerID
                        adView.setAdSize(adSize)
                        target.removeAllViews()
                        target.addView(adView)
                        adView.loadAd(adRequest)
                    }.onSuccess { return }
                        .onFailure { err -> Log.d(TAG, "showBannerAd: got error - " + err.message) }
                }
            }
        }

        fun showInterstitialAd(activity: Activity, onExit: () -> Unit) {
            Store.Ads.forEach {
                if (!isEligibleToShowInterstitial()){
                    onExit()
                    return
                }
                if (it.type == AdType.AdMob) {
                    val adRequest = AdRequest.Builder().build()
                    InterstitialAd.load(
                        activity,
                        it.interstitialID,
                        adRequest,
                        object : InterstitialAdLoadCallback() {
                            override fun onAdFailedToLoad(p0: LoadAdError) {
                                Log.d(TAG, "onAdFailedToLoad: got error - " + p0.message)
                            }
                            override fun onAdLoaded(p0: InterstitialAd) {
                                Store.lastInterstitialShowTimeUnix = System.currentTimeMillis()
                                p0.fullScreenContentCallback = object : FullScreenContentCallback() {
                                    override fun onAdDismissedFullScreenContent() {
                                        onExit()
                                    }
                                }
                                p0.show(activity)
                            }
                        })
                }
            }
        }

        private fun initGoogleAds(context: Context) {
            MobileAds.initialize(context) {}
        }
    }
}