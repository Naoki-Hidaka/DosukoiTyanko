package jp.dosukoityanko.domain.repository.restaurantList

import android.location.Location
import jp.dosukoityanko.BuildConfig
import jp.dosukoityanko.domain.entity.common.ErrorBody
import jp.dosukoityanko.domain.entity.common.Resource
import jp.dosukoityanko.domain.entity.restaurantList.Restaurant
import jp.dosukoityanko.domain.service.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

object RestaurantListDataStore {

    private const val apiKey = BuildConfig.API_KEY

    fun fetchRestaurants(
        location: Location?,
        operation: (List<Restaurant>) -> List<Restaurant> = { it }
    ): Flow<Resource<List<Restaurant>>> = flow {
        emit(Resource.InProgress)
        runCatching {
            withContext(Dispatchers.IO) {
                location?.let {
                    ApiClient.retrofit.getRestaurant(
                        apiKey,
                        it.latitude,
                        it.longitude
                    )
                } ?: run {
                    ApiClient.retrofit.getRestaurant(apiKey)
                }
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
}
