package com.dynomo.ads_manager.model

data class AdsConfig(
    val enableOpen: Boolean = false,
    val enableBanner: Boolean = false,
    val enableInterstitial: Boolean = false,
    val enableNative: Boolean = false,
    val enableReward: Boolean = false,
    val ads: List<Ad> = mutableListOf(),
)
