package jp.dosukoityanko.presentation.view.restaurantList

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import jp.dosukoityanko.R
import jp.dosukoityanko.databinding.FragmentRestaurantListBinding
import jp.dosukoityanko.databinding.ItemRestaurantListBinding
import jp.dosukoityanko.domain.entity.common.Resource
import jp.dosukoityanko.domain.entity.restaurantList.Restaurant
import jp.dosukoityanko.presentation.view.top.TopFragmentDirections
import jp.dosukoityanko.presentation.view.util.showRetryDialog
import jp.dosukoityanko.presentation.view.util.transitionPage
import jp.dosukoityanko.presentation.viewmodel.restaurantList.RestaurantListViewModel
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class RestaurantListFragment : Fragment() {

    private val viewModel: RestaurantListViewModel by hiltNavGraphViewModels(R.id.nav_graph)

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
                    }
                    is Resource.ApiError -> {
                        it.progressBar.visibility = View.GONE
                        viewModel.finalCalledFunction.value?.let {
                            showRetryDialog(
                                requireContext(),
                                it,
                                message = resource.errorBody.error.first().message
                            )
                        }
                    }
                    is Resource.NetworkError -> {
                        it.progressBar.visibility = View.GONE
                        viewModel.finalCalledFunction.value?.let {
                            showRetryDialog(
                                requireContext(),
                                it
                            )
                        }
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
                viewModel.getRestaurantBelowThreeThousand()
            }
        }
        it.searchButton2.setOnClickListener {
            getLocation {
                viewModel.getRestaurantBelowFiveThousand()
            }
        }
        it.viewModel = viewModel
        it.lifecycleOwner = viewLifecycleOwner
        it.root
    }

    private fun getLocation(
        callback: () -> Unit
    ) {

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_PERMISSION
            )
        }
        locationServices.requestLocationUpdates(
            LocationRequest.create(),
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    locationResult?.lastLocation?.let {
                        viewModel.setLocation(it)
                    }
                    callback.invoke()
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
