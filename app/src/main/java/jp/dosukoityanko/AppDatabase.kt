package jp.dosukoityanko

import androidx.room.Database
import androidx.room.RoomDatabase
import jp.dosukoityanko.data.entity.restaurantList.Restaurant
import jp.dosukoityanko.data.repository.likeList.LikeRestaurantDao

@Database(entities = [Restaurant::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun likeRestaurantDao(): LikeRestaurantDao
}
