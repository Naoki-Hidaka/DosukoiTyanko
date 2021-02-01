package com.example.myapplication.dosukoityanko.domain.repository.likeList

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.myapplication.dosukoityanko.domain.entity.restaurantList.Restaurant

@Dao
interface LikeRestaurantDao {
    @Query("select * from restaurant")
    suspend fun getAll(): List<Restaurant>

    @Query("select * from restaurant where id = :id")
    suspend fun getRestaurant(id: Int): List<Restaurant>

    @Insert
    suspend fun addRestaurant(restaurant: Restaurant)

    @Delete
    suspend fun deleteRestaurant(restaurant: Restaurant)

}
