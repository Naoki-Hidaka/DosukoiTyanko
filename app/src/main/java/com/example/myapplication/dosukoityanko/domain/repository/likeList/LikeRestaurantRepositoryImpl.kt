package com.example.myapplication.dosukoityanko.domain.repository.likeList

import com.example.myapplication.dosukoityanko.domain.entity.common.Resource
import com.example.myapplication.dosukoityanko.domain.entity.restaurantList.Restaurant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LikeRestaurantRepositoryImpl(
    private val likeRestaurantDao: LikeRestaurantDao
) : LikeRestaurantRepository {

    override suspend fun getAllRestaurant(): Flow<Resource<List<Restaurant>>> = flow {
        emit(Resource.InProgress)
        emit(Resource.Success(likeRestaurantDao.getAll()))
    }

    override suspend fun deleteRestaurant(restaurant: Restaurant) {
        likeRestaurantDao.deleteRestaurant(restaurant)
    }
}
