package com.dynomo.ads_manager.store

import com.dynomo.ads_manager.model.Ad

internal class Store {
    companion object {
        lateinit var Ads: List<Ad>
        var interstitialIntervalInSecond = 0
        var lastInterstitialShowTimeUnix: Long = 0
    }
}