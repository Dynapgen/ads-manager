package com.dynomo.adsmanager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dynomo.ads_manager.AdManager
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
                bannerID = "ca-app-pub-3940256099942544/6300978111"
            )
        )

        AdManager.init(this, ads)

        binding.btnAdBanner.setOnClickListener {
            AdManager.showBannerAd(this@MainActivity, binding.adBanner)
        }
    }
}