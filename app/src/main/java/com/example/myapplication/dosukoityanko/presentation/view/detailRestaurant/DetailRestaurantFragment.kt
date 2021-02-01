package com.example.myapplication.dosukoityanko.presentation.view.detailRestaurant

import android.os.Bundle
import android.view.*
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.example.myapplication.dosukoityanko.R
import com.example.myapplication.dosukoityanko.databinding.FragmentDetailRestaurantBinding

class DetailRestaurantFragment : Fragment() {

    companion object {
        private const val webViewUrl =
            "https://r.gnavi.co.jp/p458303/?ak=KxDKTtl%2BGwknQP4k%2Fpbc%2Bxt8hrFxrOsZwIF9HhekV8g%3D"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentDetailRestaurantBinding.inflate(inflater, container, false).let {
        it.lifecycleOwner = viewLifecycleOwner
        it.webView.apply {
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    it.progressBar.visibility = View.GONE
                }
            }
            settings.supportZoom()
            settings.builtInZoomControls = true
            loadUrl(webViewUrl)
        }
        setHasOptionsMenu(true)
        it.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_menu, menu)
    }
}
