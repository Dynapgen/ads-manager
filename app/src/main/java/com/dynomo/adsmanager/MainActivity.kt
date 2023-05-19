package com.dynomo.adsmanager

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dynomo.ads_manager.AdsManager
import com.dynomo.ads_manager.model.Ad
import com.dynomo.ads_manager.model.AdSize
import com.dynomo.ads_manager.model.AdType
import com.dynomo.adsmanager.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val ads = listOf(
            Ad(
                type = AdType.AdMob,
                adSize = AdSize.SMALL,
                bannerID = "ca-app-pub-3940256099942544/6300978111",
                interstitialID = "ca-app-pub-3940256099942544/1033173712",
                nativeID =  "ca-app-pub-3940256099942544/2247696110"
            )
        )

        AdsManager.setAdsConfig(ads, 30)

        binding.btnAdBanner.setOnClickListener {
            AdsManager.showBannerAd(this@MainActivity, binding.adBanner)
        }

        binding.btnAdInterstitial.setOnClickListener {
            AdsManager.showInterstitialAd(this@MainActivity){
                Toast.makeText(this, "Interstitial Dismissed", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnAdOpen.setOnClickListener {
            AdsManager.showOpenAd(this){
                Toast.makeText(this, "Open Ad Closed", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnAdNative.setOnClickListener {
            AdsManager.showNativeAd(this, binding.adNative)
        }
    }
}