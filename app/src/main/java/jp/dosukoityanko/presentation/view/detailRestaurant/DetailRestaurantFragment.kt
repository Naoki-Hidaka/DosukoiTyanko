package jp.dosukoityanko.presentation.view.detailRestaurant

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import jp.dosukoityanko.R
import jp.dosukoityanko.presentation.viewmodel.restaurantList.RestaurantListViewModel

class DetailRestaurantFragment : Fragment() {

    private val viewModel: RestaurantListViewModel by navGraphViewModels(R.id.nav_graph)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_detail_restaurant, container, false).apply {
        viewModel.isLoading.value = true
        setHasOptionsMenu(true)
        findViewById<ComposeView>(R.id.composeView).setContent {
            MaterialTheme {
                DetailRestaurantPage(viewModel = viewModel)
            }
        }
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
