package com.example.myapplication.dosukoityanko.presentation.view.likeList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.dosukoityanko.R
import com.example.myapplication.dosukoityanko.databinding.FragmentLikeListBinding
import com.example.myapplication.dosukoityanko.databinding.ItemRestaurantListBinding
import com.example.myapplication.dosukoityanko.domain.entity.restaurantList.Restaurant
import com.example.myapplication.dosukoityanko.domain.service.MyApplication
import com.example.myapplication.dosukoityanko.presentation.view.top.TopFragmentDirections
import com.example.myapplication.dosukoityanko.presentation.view.util.confirmDialog
import com.example.myapplication.dosukoityanko.presentation.view.util.transitionPage
import com.example.myapplication.dosukoityanko.presentation.viewmodel.likeList.LikeListViewModel
import kotlinx.coroutines.flow.collect

class LikeListFragment : Fragment() {

    private val viewModel: LikeListViewModel by navGraphViewModels(R.id.nav_graph) {
        LikeListViewModel.Companion.Factory(
            MyApplication.db.likeRestaurantDao()
        )
    }

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
