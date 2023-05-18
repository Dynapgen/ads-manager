package com.dynomo.ads_manager

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.util.Log
import android.view.ViewGroup
import android.view.WindowManager
import com.dynomo.ads_manager.model.Ad
import com.dynomo.ads_manager.model.AdSize
import com.dynomo.ads_manager.model.AdType
import com.dynomo.ads_manager.store.Store
import com.dynomo.ads_manager.util.AdMob
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds

const val TAG = "AdsManager"

class AdManager {

    companion object {
        fun init(context: Context, ads: List<Ad>) {
            Store.Ads = ads
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
                    }.onSuccess { return }.onFailure { err -> Log.d(TAG, "showBannerAd: got error - " + err.message) }
                }
            }
        }

        private fun initGoogleAds(context: Context) {
            MobileAds.initialize(context) {}
        }
    }
}