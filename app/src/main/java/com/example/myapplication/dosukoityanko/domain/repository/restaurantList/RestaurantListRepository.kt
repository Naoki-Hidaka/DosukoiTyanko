package com.example.myapplication.dosukoityanko.domain.repository.restaurantList

import com.example.myapplication.dosukoityanko.domain.entity.common.Resource
import com.example.myapplication.dosukoityanko.domain.entity.restaurantList.Restaurant
import kotlinx.coroutines.flow.Flow

interface RestaurantListRepository {

    suspend fun getRestaurant(): Flow<Resource<List<Restaurant>>>
}
