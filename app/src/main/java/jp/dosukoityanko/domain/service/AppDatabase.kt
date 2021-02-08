package jp.dosukoityanko.domain.service

import androidx.room.Database
import androidx.room.RoomDatabase
import jp.dosukoityanko.domain.entity.restaurantList.Restaurant
import jp.dosukoityanko.domain.repository.likeList.LikeRestaurantDao

@Database(entities = [Restaurant::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun likeRestaurantDao(): LikeRestaurantDao
}
