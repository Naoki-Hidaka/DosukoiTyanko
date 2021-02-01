package com.example.myapplication.dosukoityanko.domain.service

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myapplication.dosukoityanko.domain.entity.restaurantList.Restaurant
import com.example.myapplication.dosukoityanko.domain.repository.likeList.LikeRestaurantDao

@Database(entities = [Restaurant::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun likeRestaurantDao(): LikeRestaurantDao
}
