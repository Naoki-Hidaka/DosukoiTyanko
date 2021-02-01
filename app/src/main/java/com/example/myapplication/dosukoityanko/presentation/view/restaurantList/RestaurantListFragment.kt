package com.example.myapplication.dosukoityanko.presentation.view.restaurantList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.dosukoityanko.databinding.FragmentRestaurantListBinding
import com.example.myapplication.dosukoityanko.databinding.ItemRestaurantListBinding
import com.example.myapplication.dosukoityanko.domain.entity.common.Resource
import com.example.myapplication.dosukoityanko.domain.entity.restaurantList.Restaurant
import com.example.myapplication.dosukoityanko.presentation.view.top.TopFragmentDirections
import com.example.myapplication.dosukoityanko.presentation.view.util.transitionPage
import com.example.myapplication.dosukoityanko.presentation.viewmodel.restaurantList.RestaurantListViewModel
import kotlinx.coroutines.flow.collect

class RestaurantListFragment : Fragment() {

    private val viewModel: RestaurantListViewModel by viewModels {
        RestaurantListViewModel.Companion.Factory()
    }

    private val restaurantListAdapter by lazy { RestaurantListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentRestaurantListBinding.inflate(inflater, container, false).let {
        lifecycleScope.launchWhenResumed {
            viewModel.restaurantList.collect { resource ->
                when (resource) {
                    is Resource.Empty -> {
                    }
                    is Resource.InProgress -> {
                        it.progressBar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        restaurantListAdapter.submitList(resource.extractData)
                        it.progressBar.visibility = View.GONE
                        it.searchButton1.visibility = View.GONE
                        it.searchButton2.visibility = View.GONE
                        it.fab.visibility = View.VISIBLE
                    }
                    is Resource.ApiError -> {
                    }
                    is Resource.NetworkError -> {
                    }
                }
            }
        }
        it.recyclerView.apply {
            adapter = restaurantListAdapter
            layoutManager = LinearLayoutManager(context)
        }
        it.searchButton1.setOnClickListener {
            viewModel.getRestaurantList()
        }
        it.searchButton2.setOnClickListener {
            viewModel.getRestaurantList()
        }
        it.lifecycleOwner = viewLifecycleOwner
        it.root
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
                    transitionPage(
                        TopFragmentDirections.actionTopFragmentToDetailRestaurantFragment(
                            position
                        )
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
