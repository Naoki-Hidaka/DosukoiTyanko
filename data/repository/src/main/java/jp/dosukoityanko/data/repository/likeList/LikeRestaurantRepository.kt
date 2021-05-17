package jp.dosukoityanko.data.repository.likeList

import jp.dosukoityanko.data.entity.restaurantList.Restaurant
import kotlinx.coroutines.flow.Flow

interface LikeRestaurantRepository {
    suspend fun getAllRestaurant(): Flow<List<Restaurant>>

    suspend fun deleteRestaurant(restaurant: Restaurant)
}
