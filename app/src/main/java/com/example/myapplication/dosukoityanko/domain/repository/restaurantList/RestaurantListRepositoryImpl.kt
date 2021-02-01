package com.example.myapplication.dosukoityanko.domain.repository.restaurantList

import com.example.myapplication.dosukoityanko.BuildConfig
import com.example.myapplication.dosukoityanko.domain.entity.common.ErrorBody
import com.example.myapplication.dosukoityanko.domain.entity.common.Resource
import com.example.myapplication.dosukoityanko.domain.entity.restaurantList.Restaurant
import com.example.myapplication.dosukoityanko.domain.service.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import timber.log.Timber

object RestaurantListRepositoryImpl : RestaurantListRepository {

    private const val apiKey = BuildConfig.API_KEY

    override suspend fun getRestaurant(): Flow<Resource<List<Restaurant>>> = flow {
        emit(Resource.InProgress)
        runCatching {
            withContext(Dispatchers.IO) {
                ApiClient.retrofit.getRestaurant(apiKey)
            }
        }
            .onSuccess {
                if (it.isSuccessful) {
                    Timber.d("debug: success ${it.body()}")
                    it.body()?.rest?.let {
                        emit(Resource.Success(it))
                    }
                } else {
                    Timber.d("debug: apiError ${it.errorBody()}")
                    emit(Resource.ApiError(ErrorBody.fromJson(it.errorBody()?.string())))
                }
            }
            .onFailure {
                Timber.d("debug: networkError $it")
                emit(Resource.NetworkError(it))
            }
    }.flowOn(Dispatchers.IO)
}
