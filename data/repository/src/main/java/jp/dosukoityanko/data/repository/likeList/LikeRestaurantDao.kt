package jp.dosukoityanko.data.repository.likeList

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import jp.dosukoityanko.data.entity.restaurantList.Restaurant
import kotlinx.coroutines.flow.Flow

@Dao
interface LikeRestaurantDao {
    @Query("select * from restaurant")
    fun getAll(): Flow<List<Restaurant>>

    @Query("select * from restaurant where id = :id")
    suspend fun getRestaurant(id: String): Restaurant?

    @Insert
    suspend fun addRestaurant(restaurant: Restaurant)

    @Delete
    suspend fun deleteRestaurant(restaurant: Restaurant)
}
