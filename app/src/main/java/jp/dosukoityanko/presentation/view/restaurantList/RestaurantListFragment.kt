package jp.dosukoityanko.presentation.view.restaurantList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.livedata.observeAsState
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import jp.dosukoityanko.R
import jp.dosukoityanko.databinding.FragmentRestaurantListBinding
import jp.dosukoityanko.databinding.ItemRestaurantListBinding
import jp.dosukoityanko.domain.entity.restaurantList.Restaurant
import jp.dosukoityanko.presentation.view.top.TopFragmentDirections
import jp.dosukoityanko.presentation.view.util.ProgressIndicator
import jp.dosukoityanko.presentation.view.util.showRetryDialog
import jp.dosukoityanko.presentation.view.util.transitionPage
import jp.dosukoityanko.presentation.viewmodel.restaurantList.RestaurantListViewModel
import jp.dosukoityanko.presentation.viewmodel.restaurantList.RestaurantListViewModel.Event

@AndroidEntryPoint
class RestaurantListFragment : Fragment() {

    private val viewModel: RestaurantListViewModel by hiltNavGraphViewModels(R.id.nav_graph)

    private val restaurantListAdapter by lazy { RestaurantListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentRestaurantListBinding.inflate(inflater, container, false).let {
        it.progressIndicator.setContent {
            ProgressIndicator(isLoading = viewModel.isLoading.observeAsState().value)
        }
        it.empty.setContent {
            EmptyState(viewModel.emptyImageState.observeAsState().value)
        }
        it.notFoundText.setContent {
            NotFoundText(viewModel.isNotFoundTextVisible.observeAsState().value)
        }
        viewModel.onEvent.observe(viewLifecycleOwner, ::handleEvent)
        it.recyclerView.apply {
            adapter = restaurantListAdapter
            layoutManager = LinearLayoutManager(context)
        }
        it.viewModel = viewModel
        it.lifecycleOwner = viewLifecycleOwner
        it.root
    }

    private fun handleEvent(event: Event) {
        when (event) {
            Event.ClickedSearchButton -> {
                viewModel.getRestaurant()
            }
            is Event.FetchSuccess -> {
                restaurantListAdapter.submitList(event.restaurantList)
            }
            Event.ApiError -> {
                showRetryDialog(requireContext(), {
                    viewModel.getRestaurant()
                })

            }
            Event.NetworkError -> {
                showRetryDialog(requireContext(), {
                    viewModel.getRestaurant()
                })

            }
        }
    }

    private inner class RestaurantListAdapter :
        ListAdapter<Restaurant, RestaurantViewHolder>(RestaurantListDiff()) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder =
            RestaurantViewHolder(
                ItemRestaurantListBinding.inflate(
                    LayoutInflater.from(context),
                    parent,
                    false
                )
            )

        override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
            holder.binding.also {
                it.lifecycleOwner = viewLifecycleOwner
                it.restaurant = getItem(position)
                it.container.setOnClickListener {
                    viewModel.onSelectRestaurant(getItem(position))
                    transitionPage(
                        TopFragmentDirections.actionTopFragmentToDetailRestaurantFragment()
                    )
                }
            }
        }
    }

    private inner class RestaurantViewHolder(val binding: ItemRestaurantListBinding) :
        RecyclerView.ViewHolder(binding.root)

    private inner class RestaurantListDiff : DiffUtil.ItemCallback<Restaurant>() {
        override fun areItemsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean =
            oldItem == newItem
    }
}
