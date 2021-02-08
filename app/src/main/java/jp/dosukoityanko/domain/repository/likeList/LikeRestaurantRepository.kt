package jp.dosukoityanko.domain.repository.likeList

import jp.dosukoityanko.domain.entity.restaurantList.Restaurant
import kotlinx.coroutines.flow.Flow

interface LikeRestaurantRepository {
    suspend fun getAllRestaurant(): Flow<List<Restaurant>>

    suspend fun deleteRestaurant(restaurant: Restaurant)
}
