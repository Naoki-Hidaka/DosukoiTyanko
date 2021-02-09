package jp.dosukoityanko.domain.repository.likeList

import jp.dosukoityanko.domain.entity.restaurantList.Restaurant
import kotlinx.coroutines.flow.Flow

class LikeRestaurantRepositoryImpl(
    private val likeRestaurantDao: LikeRestaurantDao
) : LikeRestaurantRepository {

    override suspend fun getAllRestaurant(): Flow<List<Restaurant>> = likeRestaurantDao.getAll()

    override suspend fun deleteRestaurant(restaurant: Restaurant) {
        likeRestaurantDao.deleteRestaurant(restaurant)
    }
}
