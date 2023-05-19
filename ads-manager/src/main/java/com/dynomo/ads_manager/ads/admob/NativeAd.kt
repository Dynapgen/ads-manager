package com.dynomo.ads_manager.ads.admob

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.dynomo.ads_manager.R
import com.google.android.gms.ads.nativead.NativeAdView
import com.google.android.gms.ads.nativead.NativeAd as Ad

internal class NativeAd {
    companion object {
        fun populateNativeAdView(ad: Ad, adView: NativeAdView) {
            adView.mediaView = adView.findViewById(R.id.ad_media)
            adView.headlineView = adView.findViewById(R.id.ad_headline)
            adView.bodyView = adView.findViewById(R.id.ad_body)
            adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
            adView.iconView = adView.findViewById(R.id.ad_app_icon)
            adView.priceView = adView.findViewById(R.id.ad_price)
            adView.starRatingView = adView.findViewById(R.id.ad_stars)
            adView.storeView = adView.findViewById(R.id.ad_store)
            adView.advertiserView = adView.findViewById(R.id.ad_advertiser)

            adView.headlineView?.let { (it as TextView).text = ad.headline }
            ad.mediaContent?.let { adView.mediaView?.mediaContent = it }

            adView.bodyView?.visibility = View.INVISIBLE
            ad.body?.let {
                adView.bodyView?.apply {
                    visibility = View.VISIBLE
                    (this as TextView).text = it
                }
            }

            adView.callToActionView?.visibility = View.INVISIBLE
            ad.callToAction?.let {
                adView.callToActionView?.apply {
                    visibility = View.VISIBLE
                    (this as Button).text = it
                }
            }

            adView.iconView?.visibility = View.INVISIBLE
            ad.icon?.let {
                adView.iconView?.apply {
                    visibility = View.VISIBLE
                    (this as ImageView).setImageDrawable(it.drawable)
                }
            }

            adView.priceView?.visibility = View.INVISIBLE
            ad.price?.let {
                adView.priceView?.apply {
                    visibility = View.VISIBLE
                    (this as TextView).text = it
                }
            }

            adView.storeView?.visibility = View.INVISIBLE
            ad.store?.let {
                adView.storeView?.apply {
                    visibility = View.VISIBLE
                    (this as TextView).text = it
                }
            }

            adView.starRatingView?.visibility = View.INVISIBLE
            ad.starRating?.let {
                adView.starRatingView?.apply {
                    visibility = View.VISIBLE
                    (this as RatingBar).rating = it.toFloat()
                }
            }

            adView.advertiserView?.visibility = View.INVISIBLE
            ad.advertiser?.let {
                adView.advertiserView?.apply {
                    visibility = View.VISIBLE
                    (this as TextView).text = it
                }
            }

            adView.setNativeAd(ad)
        }
    }
}