package com.example.myapplication.dosukoityanko.presentation.view.restaurantList

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.dosukoityanko.R
import com.example.myapplication.dosukoityanko.databinding.FragmentRestaurantListBinding
import com.example.myapplication.dosukoityanko.databinding.ItemRestaurantListBinding
import com.example.myapplication.dosukoityanko.domain.entity.common.Resource
import com.example.myapplication.dosukoityanko.domain.entity.restaurantList.Restaurant
import com.example.myapplication.dosukoityanko.domain.service.MyApplication
import com.example.myapplication.dosukoityanko.presentation.view.top.TopFragmentDirections
import com.example.myapplication.dosukoityanko.presentation.view.util.showRetryDialog
import com.example.myapplication.dosukoityanko.presentation.view.util.transitionPage
import com.example.myapplication.dosukoityanko.presentation.viewmodel.restaurantList.RestaurantListViewModel
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.collect

class RestaurantListFragment : Fragment() {

    private val viewModel: RestaurantListViewModel by navGraphViewModels(R.id.nav_graph) {
        RestaurantListViewModel.Companion.Factory(
            activity?.application,
            MyApplication.db.likeRestaurantDao()
        )
    }

    private val restaurantListAdapter by lazy { RestaurantListAdapter() }

    private val locationServices by lazy {
        LocationServices.getFusedLocationProviderClient(requireContext())
    }

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
                        viewModel.setEmptyImageState(false)
                        it.progressBar.visibility = View.VISIBLE
                        it.emptyImage.visibility = View.GONE
                    }
                    is Resource.Success -> {
                        restaurantListAdapter.submitList(resource.extractData)
                        it.progressBar.visibility = View.GONE
                        it.searchButton.visibility = View.VISIBLE
                    }
                    is Resource.ApiError -> {
                        showRetryDialog(requireContext(), viewModel::getRestaurantList)
                    }
                    is Resource.NetworkError -> {
                        showRetryDialog(requireContext(), viewModel::getRestaurantList)
                    }
                }
            }
        }
        it.recyclerView.apply {
            adapter = restaurantListAdapter
            layoutManager = LinearLayoutManager(context)
        }
        it.searchButton1.setOnClickListener {
            getLocation {
                viewModel.getRestaurantBelowThreeThousand(it)
            }
        }
        it.searchButton2.setOnClickListener {
            getLocation {
                viewModel.getRestaurantBelowFiveThousand(it)
            }
        }
        it.viewModel = viewModel
        it.lifecycleOwner = viewLifecycleOwner
        it.root
    }

    private fun getLocation(
        callback: (Location) -> Unit
    ) {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                REQUEST_PERMISSION
            )
        }
        locationServices.requestLocationUpdates(
            LocationRequest.create(),
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    locationResult?.lastLocation?.let(callback)
                    locationServices.removeLocationUpdates(this)
                }
            },
            null
        )
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

    companion object {
        private const val REQUEST_PERMISSION = 1001
    }
}
