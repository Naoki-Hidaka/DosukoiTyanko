package jp.dosukoityanko.presentation.view.likeList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import jp.dosukoityanko.R
import jp.dosukoityanko.databinding.FragmentLikeDetailBinding
import jp.dosukoityanko.presentation.viewmodel.likeList.LikeListViewModel

class LikeDetailFragment : Fragment() {

    private val viewModel: LikeListViewModel by navGraphViewModels(R.id.nav_graph)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentLikeDetailBinding.inflate(layoutInflater, container, false).let {
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
        it.root
    }
}
