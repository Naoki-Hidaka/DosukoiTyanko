package jp.dosukoityanko.presentation.view.restaurantList

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.livedata.observeAsState
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import dagger.hilt.android.AndroidEntryPoint
import jp.dosukoityanko.R
import jp.dosukoityanko.databinding.FragmentRestaurantListBinding
import jp.dosukoityanko.databinding.ItemRestaurantListBinding
import jp.dosukoityanko.domain.entity.common.Resource
import jp.dosukoityanko.domain.entity.restaurantList.Restaurant
import jp.dosukoityanko.presentation.view.top.TopFragmentDirections
import jp.dosukoityanko.presentation.view.util.ProgressIndicator
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

    private val locationRequest by lazy {
        LocationRequest().apply { priority = PRIORITY_HIGH_ACCURACY }
    }

    private val locationBuilder by lazy {
        LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest).build()
    }

    private val client by lazy {
        LocationServices.getSettingsClient(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentRestaurantListBinding.inflate(inflater, container, false).let {
        it.progressIndicator.setContent {
            ProgressIndicator(isLoading = viewModel.isLoading.observeAsState().value)
        }
        it.empty.setContent {
            EmptyState(isVisible = viewModel.emptyImageState.observeAsState().value)
        }
        lifecycleScope.launchWhenResumed {
            viewModel.restaurantList.collect { resource ->
                when (resource) {
                    is Resource.Empty -> {
                        it.notFoundMessage.visibility = View.GONE
                    }
                    is Resource.InProgress -> {
                        it.notFoundMessage.visibility = View.GONE
                    }
                    is Resource.Success -> {
                        it.notFoundMessage.visibility = View.GONE
                        restaurantListAdapter.submitList(resource.extractData)
                    }
                    is Resource.ApiError -> {
                        it.notFoundMessage.visibility = View.VISIBLE
                        viewModel.finalCalledFunction.value?.let {
                            showRetryDialog(
                                requireContext(),
                                it,
                                message = resource.errorBody.errorString()
                            )
                        }
                    }
                    is Resource.NetworkError -> {
                        it.notFoundMessage.visibility = View.GONE
                        viewModel.finalCalledFunction.value?.let {
                            showRetryDialog(
                                requireContext(),
                                it,
                                "ネットワークエラー",
                                resource.errorMessage
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

        it.bottomSheet.searchButton.setOnClickListener {
            getLocation {
                viewModel.getRestaurant()
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
        client.checkLocationSettings(locationBuilder)
            .addOnSuccessListener {
                locationServices.requestLocationUpdates(
                    locationRequest,
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
            .addOnFailureListener {
                if (it is ResolvableApiException) {
                    it.startResolutionForResult(requireActivity(), 1)
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

    companion object {
        private const val REQUEST_PERMISSION = 1001
    }
}
