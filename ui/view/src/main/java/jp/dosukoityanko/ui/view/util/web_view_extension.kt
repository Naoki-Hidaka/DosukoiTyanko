package jp.dosukoityanko.ui.view.util

import android.webkit.WebView
import androidx.databinding.BindingAdapter

@BindingAdapter("url")
fun WebView.loadPage(url: String) {
    loadUrl(url)
}
