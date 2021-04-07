package jp.dosukoityanko.presentation.view.likeList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import jp.dosukoityanko.R
import jp.dosukoityanko.presentation.viewmodel.likeList.LikeListViewModel

class LikeDetailFragment : Fragment() {

    private val viewModel: LikeListViewModel by navGraphViewModels(R.id.nav_graph)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_detail_restaurant, container, false).apply {
        findViewById<ComposeView>(R.id.composeView).setContent {
            MaterialTheme {
                LikeDetailPage(viewModel)
            }
        }
    }
}
