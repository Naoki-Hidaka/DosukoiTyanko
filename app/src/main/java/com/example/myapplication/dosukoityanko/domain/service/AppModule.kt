package com.example.myapplication.dosukoityanko.domain.service

import android.content.Context
import androidx.room.Room
import com.example.myapplication.dosukoityanko.domain.repository.likeList.LikeRestaurantDao
import com.example.myapplication.dosukoityanko.domain.repository.likeList.LikeRestaurantRepository
import com.example.myapplication.dosukoityanko.domain.repository.likeList.LikeRestaurantRepositoryImpl
import com.example.myapplication.dosukoityanko.domain.repository.restaurantList.RestaurantListRepository
import com.example.myapplication.dosukoityanko.domain.repository.restaurantList.RestaurantListRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "AppDatabase"
        ).build()
    }

    @Provides
    @Singleton
    fun provideLikeDao(db: AppDatabase) = db.likeRestaurantDao()

    @Provides
    @Singleton
    fun provideRestaurantListRepository(
        likeRestaurantDao: LikeRestaurantDao
    ): RestaurantListRepository {
        return RestaurantListRepositoryImpl(likeRestaurantDao)
    }

    @Provides
    @Singleton
    fun provideLikeListRepository(
        likeRestaurantDao: LikeRestaurantDao
    ): LikeRestaurantRepository = LikeRestaurantRepositoryImpl(likeRestaurantDao)
}
