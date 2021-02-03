package com.example.myapplication.dosukoityanko.domain.repository.restaurantList

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

    override suspend fun getRestaurantBelowThousand(): Flow<Resource<List<Restaurant>>> = flow {
        emit(Resource.InProgress)
        runCatching {
            withContext(Dispatchers.IO) {
                ApiClient.retrofit.getRestaurant(apiKey)
            }
        }
            .onSuccess {
                if (it.isSuccessful) {
                    it.body()?.rest?.let {
                        it.dropWhile {
                            it.budget?.toInt() ?: 0 > 3000
                        }.let {
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

    override suspend fun getRestaurantBelowThreeThousand(): Flow<Resource<List<Restaurant>>> =
        flow {
            emit(Resource.InProgress)
            runCatching {
                withContext(Dispatchers.IO) {
                    ApiClient.retrofit.getRestaurant(apiKey)
                }
            }
                .onSuccess {
                    if (it.isSuccessful) {
                        it.body()?.rest?.let {
                            it.dropWhile {
                                it.budget?.toInt() ?: 0 > 5000
                            }.let {
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
