package com.dynomo.ads_manager.store

import com.dynomo.ads_manager.model.Ad
import com.dynomo.ads_manager.model.AdsConfig

internal class Store {
    companion object {
        var adsConfig: AdsConfig = AdsConfig()
        var interstitialIntervalInSecond = 0
        var lastInterstitialShowTimeUnix: Long = 0
    }
}