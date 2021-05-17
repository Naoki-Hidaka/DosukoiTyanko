package jp.dosukoityanko.data.repository.restaurantList

import android.location.Location
import jp.dosukoityanko.data.entity.common.Amount
import jp.dosukoityanko.data.entity.common.Distance
import jp.dosukoityanko.data.entity.common.Resource
import jp.dosukoityanko.data.entity.restaurantList.Restaurant
import jp.dosukoityanko.data.repository.likeList.LikeRestaurantDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RestaurantListRepositoryImpl @Inject constructor(
    private val likeRestaurantDao: LikeRestaurantDao,
    private val restaurantListDataStore: RestaurantListDataStore
) : RestaurantListRepository {

    override suspend fun getRestaurant(
        location: Location?,
        distance: Distance?,
        amount: Amount?
    ): Flow<Resource<List<Restaurant>>> =
        restaurantListDataStore.fetchRestaurants(location, distance) {
            it.filter { it.budgetInt() <= amount?.value ?: 3000 }
        }

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
