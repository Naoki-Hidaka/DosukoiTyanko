package jp.dosukoityanko.data.repository.likeList

import jp.dosukoityanko.data.entity.restaurantList.Restaurant
import kotlinx.coroutines.flow.Flow

class LikeRestaurantRepositoryImpl(
    private val likeRestaurantDao: LikeRestaurantDao
) : LikeRestaurantRepository {

    override suspend fun getAllRestaurant(): Flow<List<Restaurant>> = likeRestaurantDao.getAll()

    override suspend fun deleteRestaurant(restaurant: Restaurant) {
        likeRestaurantDao.deleteRestaurant(restaurant)
    }
}
