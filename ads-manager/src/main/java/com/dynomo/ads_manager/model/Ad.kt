package com.dynomo.ads_manager.model

data class Ad(
    val type: AdType = AdType.Unset,

    val adSize: AdSize = AdSize.MEDIUM,
    val nativeAdSize: NativeAdSize = NativeAdSize.SMALL,
    val bannerID: String = "",
    val interstitialID: String = "",
    val openID: String = "",
    val nativeID: String = ""
)