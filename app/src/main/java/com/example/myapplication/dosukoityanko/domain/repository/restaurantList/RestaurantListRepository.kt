package com.example.myapplication.dosukoityanko.domain.repository.restaurantList

import android.location.Location
import com.example.myapplication.dosukoityanko.domain.entity.common.Resource
import com.example.myapplication.dosukoityanko.domain.entity.restaurantList.Restaurant
import kotlinx.coroutines.flow.Flow

interface RestaurantListRepository {

    suspend fun getRestaurant(): Flow<Resource<List<Restaurant>>>

    suspend fun getRestaurantBelowThreeThousand(location: Location): Flow<Resource<List<Restaurant>>>

    suspend fun getRestaurantBelowFiveThousand(location: Location): Flow<Resource<List<Restaurant>>>

    suspend fun addRestaurant(
        restaurant: Restaurant,
        callback: () -> Unit,
        fallback: (Throwable) -> Unit
    )
}
