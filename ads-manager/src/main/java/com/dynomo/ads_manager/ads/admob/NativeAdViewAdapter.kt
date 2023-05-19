package com.dynomo.ads_manager.ads.admob

import android.view.View
import android.widget.TextView
import com.google.android.gms.ads.nativead.MediaView

data class NativeView (
    val mediaView: MediaView?,
    val headlineView: TextView?,
    val bodyView: View?,
    val callToActionView: View?,
    val iconView: View?,
    val priceView: View?,
    val starRatingView: View?,
    val storeView: View?,
    val advertiserView: View?,
    )