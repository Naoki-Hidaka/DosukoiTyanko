package jp.dosukoityanko.ui.view.restaurantList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.api.ResolvableApiException
import dagger.hilt.android.AndroidEntryPoint
import jp.dosukoityanko.data.entity.restaurantList.Restaurant
import jp.dosukoityanko.ui.view.R
import jp.dosukoityanko.ui.view.databinding.FragmentRestaurantListBinding
import jp.dosukoityanko.ui.view.databinding.ItemRestaurantListBinding
import jp.dosukoityanko.ui.view.top.TopFragmentDirections
import jp.dosukoityanko.ui.view.util.showRetryDialog
import jp.dosukoityanko.ui.view.util.transitionPage
import jp.dosukoityanko.ui.viewmodel.restaurantList.RestaurantListViewModel

@AndroidEntryPoint
class RestaurantListFragment : Fragment() {

    private val viewModel: RestaurantListViewModel by hiltNavGraphViewModels(R.id.nav_graph)

    private val restaurantListAdapter by lazy { RestaurantListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentRestaurantListBinding.inflate(inflater, container, false).let {
        viewModel.onEvent.observe(viewLifecycleOwner, ::handleEvent)
        it.recyclerView.apply {
            adapter = restaurantListAdapter
            layoutManager = LinearLayoutManager(context)
        }
        it.viewModel = viewModel
        it.lifecycleOwner = viewLifecycleOwner
        it.root
    }

    private fun handleEvent(event: RestaurantListViewModel.ApiEvent) {
        when (event) {
            is RestaurantListViewModel.ApiEvent.Success -> {
                restaurantListAdapter.submitList(event.restaurantList)
            }
            is RestaurantListViewModel.ApiEvent.Error -> {
                showRetryDialog(
                    requireContext(),
                    viewModel.finalCalledFunction.value,
                    message = event.errorText
                )
            }
            is RestaurantListViewModel.ApiEvent.LocationError -> {
                val exception = event.exception
                if (exception is ResolvableApiException) {
                    exception.startResolutionForResult(requireActivity(), 1)
                }
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
                    viewModel.selectRestaurant(position)
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
