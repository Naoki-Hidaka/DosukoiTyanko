package jp.dosukoityanko.data.repository.restaurantList

import android.location.Location
import jp.dosukoityanko.data.api.IApiType
import jp.dosukoityanko.data.entity.common.Distance
import jp.dosukoityanko.data.entity.common.ErrorBody
import jp.dosukoityanko.data.entity.common.Resource
import jp.dosukoityanko.data.entity.restaurantList.Restaurant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RestaurantListDataStoreImpl @Inject constructor(
    private val apiType: IApiType,
    private val apiKey: String
) : RestaurantListDataStore {

    override fun fetchRestaurants(
        location: Location?,
        distance: Distance?,
        operation: (List<Restaurant>) -> List<Restaurant>
    ): Flow<Resource<List<Restaurant>>> = flow {
        emit(Resource.InProgress)
        runCatching {
            withContext(Dispatchers.IO) {
                location?.let {
                    apiType.getRestaurant(
                        apiKey,
                        it.latitude,
                        it.longitude,
                        range = distance?.id ?: 3
                    )
                } ?: run {
                    apiType.getRestaurant(apiKey)
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
                emit(Resource.NetworkError(it, "ネットワーク接続を確認してください"))
            }
    }.flowOn(Dispatchers.IO)
}
