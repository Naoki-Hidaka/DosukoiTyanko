package com.example.myapplication.dosukoityanko.domain.repository.restaurantList

import android.location.Location
import com.example.myapplication.dosukoityanko.BuildConfig
import com.example.myapplication.dosukoityanko.domain.entity.common.ErrorBody
import com.example.myapplication.dosukoityanko.domain.entity.common.Resource
import com.example.myapplication.dosukoityanko.domain.entity.restaurantList.Restaurant
import com.example.myapplication.dosukoityanko.domain.repository.likeList.LikeRestaurantDao
import com.example.myapplication.dosukoityanko.domain.service.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class RestaurantListRepositoryImpl(
    private val likeRestaurantDao: LikeRestaurantDao
) : RestaurantListRepository {

    companion object {
        private const val apiKey = BuildConfig.API_KEY
    }

    override suspend fun getRestaurant(): Flow<Resource<List<Restaurant>>> = flow {
        emit(Resource.InProgress)
        runCatching {
            withContext(Dispatchers.IO) {
                ApiClient.retrofit.getRestaurant(apiKey)
            }
        }
            .onSuccess {
                if (it.isSuccessful) {
                    it.body()?.rest?.let {
                        emit(Resource.Success(it))
                    }
                } else {
                    emit(Resource.ApiError(ErrorBody.fromJson(it.errorBody()?.string())))
                }
            }
            .onFailure {
                emit(Resource.NetworkError(it))
            }
    }.flowOn(Dispatchers.IO)

    override suspend fun getRestaurantBelowThreeThousand(location: Location): Flow<Resource<List<Restaurant>>> {
        return fetchRestaurants(location) {
            it.dropWhile { it.budget?.toInt() ?: 0 >= 3000 }
        }
    }

    override suspend fun getRestaurantBelowFiveThousand(location: Location): Flow<Resource<List<Restaurant>>> =
        fetchRestaurants(location) {
            it.dropWhile { it.budget?.toInt() ?: 0 >= 5000 }
        }

    private fun fetchRestaurants(
        location: Location,
        operation: (List<Restaurant>) -> List<Restaurant> = { it }
    ): Flow<Resource<List<Restaurant>>> = flow {
        emit(Resource.InProgress)
        runCatching {
            withContext(Dispatchers.IO) {
                ApiClient.retrofit.getRestaurant(apiKey, location.latitude, location.longitude)
            }
        }
            .onSuccess {
                if (it.isSuccessful) {
                    it.body()?.rest?.let {
                        operation(it).let {
                            emit(Resource.Success(it))
                        }
                    }
                } else {
                    emit(Resource.ApiError(ErrorBody.fromJson(it.errorBody()?.string())))
                }
            }
            .onFailure {
                emit(Resource.NetworkError(it))
            }
    }.flowOn(Dispatchers.IO)

    override suspend fun addRestaurant(
        restaurant: Restaurant,
        callback: () -> Unit,
        fallback: (Throwable) -> Unit
    ) {
        runCatching {
            likeRestaurantDao.addRestaurant(restaurant)
        }
            .onSuccess {
                callback()
            }
            .onFailure {
                fallback(it)
            }
    }
}
