package jp.dosukoityanko.ui.view.likeList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import jp.dosukoityanko.data.entity.restaurantList.Restaurant
import jp.dosukoityanko.ui.view.R
import jp.dosukoityanko.ui.view.databinding.FragmentLikeListBinding
import jp.dosukoityanko.ui.view.databinding.ItemRestaurantListBinding
import jp.dosukoityanko.ui.view.top.TopFragmentDirections
import jp.dosukoityanko.ui.view.util.confirmDialog
import jp.dosukoityanko.ui.view.util.transitionPage
import jp.dosukoityanko.ui.viewmodel.likeRestaurant.LikeListViewModel
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class LikeListFragment : Fragment() {

    private val viewModel: LikeListViewModel by hiltNavGraphViewModels(R.id.nav_graph)

    private val likeListAdapter by lazy { LikeListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentLikeListBinding.inflate(inflater, container, false).let {
        lifecycleScope.launchWhenResumed {
            viewModel.likeList.collect { resource ->
                likeListAdapter.submitList(resource)
            }
        }
        it.recyclerView.apply {
            adapter = likeListAdapter
            layoutManager = LinearLayoutManager(context)
        }

        it.lifecycleOwner = viewLifecycleOwner
        it.root
    }

    private inner class LikeListAdapter : ListAdapter<Restaurant, LikeViewHolder>(LikeListDiff()) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikeViewHolder =
            LikeViewHolder(
                ItemRestaurantListBinding.inflate(
                    LayoutInflater.from(context),
                    parent,
                    false
                )
            )

        override fun onBindViewHolder(holder: LikeViewHolder, position: Int) {
            holder.binding.also {
                val data = currentList[position]
                it.lifecycleOwner = viewLifecycleOwner
                it.restaurant = data
                it.container.setOnClickListener {
                    viewModel.selectRestaurant(position)
                    transitionPage(TopFragmentDirections.actionTopFragmentToLikeDetailFragment())
                }
                it.container.setOnLongClickListener {
                    confirmDialog(requireContext(), "本当に削除しますか？") {
                        viewModel.deleteRestaurant(data)
                    }
                    true
                }
            }
        }
    }

    private inner class LikeViewHolder(val binding: ItemRestaurantListBinding) :
        RecyclerView.ViewHolder(binding.root)

    private inner class LikeListDiff : DiffUtil.ItemCallback<Restaurant>() {
        override fun areItemsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean =
            oldItem == newItem
    }
}
