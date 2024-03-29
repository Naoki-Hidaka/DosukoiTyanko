package jp.dosukoityanko.ui.view.detailRestaurant

import android.os.Bundle
import android.view.*
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import jp.dosukoityanko.ui.view.R
import jp.dosukoityanko.ui.view.databinding.FragmentDetailRestaurantBinding
import jp.dosukoityanko.ui.viewmodel.restaurantList.RestaurantListViewModel

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.star -> {
                viewModel.clickLike({
                    Toast.makeText(context, "お気に入り追加しました", Toast.LENGTH_SHORT).show()
                }
                ) {
                    Toast.makeText(context, "お気に入り追加に失敗しました", Toast.LENGTH_SHORT).show()
                }
            }
            android.R.id.home -> {
                findNavController().navigateUp()
            }
        }
        return true
    }
}
