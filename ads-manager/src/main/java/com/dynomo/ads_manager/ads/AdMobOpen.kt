package com.dynomo.ads_manager.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.core.content.PackageManagerCompat.LOG_TAG
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd

interface OnShowAdCompleteListener {
    fun onShowAdComplete()
}

class AdMobOpen {
    private var appOpenAd: AppOpenAd? = null
    private var isLoadingAd = false
    var isShowingAd = false

    /** Request an ad. */
    fun loadAd(context: Context) {
        // Do not load ad if there is an unused ad or one is already loading.
        if (isLoadingAd || isAdAvailable()) {
            return
        }

        isLoadingAd = true
        val request = AdRequest.Builder().build()
        AppOpenAd.load(
            context, "ca-app-pub-3940256099942544/3419835294", request,
            AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
            object : AppOpenAd.AppOpenAdLoadCallback() {

                override fun onAdLoaded(ad: AppOpenAd) {
                    appOpenAd = ad
                    isLoadingAd = false
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    isLoadingAd = false;
                }
            })
    }

    /** Shows the ad if one isn't already showing. */
    fun showAdIfAvailable(
        activity: Activity,
        onComplete: () -> Unit) {
        if (isShowingAd) {
            return
        }

        if (!isAdAvailable()) {
            onComplete()
            loadAd(activity)
            return
        }

        appOpenAd?.setFullScreenContentCallback(
            object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    appOpenAd = null
                    isShowingAd = false

                    Log.d("HEHEHE", "onAdFailedToShowFullScreenContent: DISMISSED" )
                    onComplete()
                    loadAd(activity)
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    appOpenAd = null
                    isShowingAd = false

                    Log.d("HEHEHE", "onAdFailedToShowFullScreenContent: " + adError.message)
                    onComplete()
                    loadAd(activity)
                }
            })
        isShowingAd = true
        appOpenAd?.show(activity)
    }

    /** Check if ad exists and can be shown. */
    private fun isAdAvailable(): Boolean {
        return appOpenAd != null
    }
}