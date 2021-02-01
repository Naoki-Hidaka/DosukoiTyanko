package com.example.myapplication.dosukoityanko.presentation.view.detailRestaurant

import android.os.Bundle
import android.view.*
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import com.example.myapplication.dosukoityanko.R
import com.example.myapplication.dosukoityanko.databinding.FragmentDetailRestaurantBinding
import com.example.myapplication.dosukoityanko.presentation.viewmodel.restaurantList.RestaurantListViewModel

class DetailRestaurantFragment : Fragment() {

    private val viewModel: RestaurantListViewModel by navGraphViewModels(R.id.nav_graph)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentDetailRestaurantBinding.inflate(inflater, container, false).let {
        it.lifecycleOwner = viewLifecycleOwner
        it.viewModel = viewModel
        it.webView.apply {
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    it.progressBar.visibility = View.GONE
                }
            }
            settings.supportZoom()
            settings.builtInZoomControls = true
        }
        setHasOptionsMenu(true)
        it.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_menu, menu)
    }
}
