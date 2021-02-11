package jp.dosukoityanko.domain.repository.restaurantList

import android.location.Location
import jp.dosukoityanko.domain.entity.common.Resource
import jp.dosukoityanko.domain.entity.restaurantList.Restaurant
import jp.dosukoityanko.domain.repository.likeList.LikeRestaurantDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RestaurantListRepositoryImpl @Inject constructor(
    private val likeRestaurantDao: LikeRestaurantDao,
    private val restaurantListDataStore: RestaurantListDataStore
) : RestaurantListRepository {

    override suspend fun getRestaurant(): Flow<Resource<List<Restaurant>>> =
        restaurantListDataStore.fetchRestaurants(null)

    override suspend fun getRestaurantBelowThreeThousand(location: Location?): Flow<Resource<List<Restaurant>>> {
        return restaurantListDataStore.fetchRestaurants(location) {
            it.filter { (it.budget?.toInt() ?: 0 <= 3000) }
        }
    }

    override suspend fun getRestaurantBelowFiveThousand(location: Location?): Flow<Resource<List<Restaurant>>> =
        restaurantListDataStore.fetchRestaurants(location) {
            it.filter { it.budget?.toInt() ?: 0 <= 5000 }
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
