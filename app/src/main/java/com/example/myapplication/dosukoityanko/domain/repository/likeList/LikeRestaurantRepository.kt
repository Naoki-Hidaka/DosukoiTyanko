package com.example.myapplication.dosukoityanko.domain.repository.likeList

import com.example.myapplication.dosukoityanko.domain.entity.common.Resource
import com.example.myapplication.dosukoityanko.domain.entity.restaurantList.Restaurant
import kotlinx.coroutines.flow.Flow

interface LikeRestaurantRepository {
    suspend fun getAllRestaurant(): Flow<Resource<List<Restaurant>>>

    suspend fun deleteRestaurant(restaurant: Restaurant)
}
