package jp.dosukoityanko.domain.service

import android.content.Context
import androidx.room.Room
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.SettingsClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jp.dosukoityanko.domain.repository.likeList.LikeRestaurantDao
import jp.dosukoityanko.domain.repository.likeList.LikeRestaurantRepository
import jp.dosukoityanko.domain.repository.likeList.LikeRestaurantRepositoryImpl
import jp.dosukoityanko.domain.repository.restaurantList.RestaurantListDataStore
import jp.dosukoityanko.domain.repository.restaurantList.RestaurantListDataStoreImpl
import jp.dosukoityanko.domain.repository.restaurantList.RestaurantListRepository
import jp.dosukoityanko.domain.repository.restaurantList.RestaurantListRepositoryImpl
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
    fun provideRestaurantListDataStore(): RestaurantListDataStore = RestaurantListDataStoreImpl

    @Provides
    @Singleton
    fun provideRestaurantListRepository(
        likeRestaurantDao: LikeRestaurantDao,
        restaurantListDataStore: RestaurantListDataStore
    ): RestaurantListRepository {
        return RestaurantListRepositoryImpl(likeRestaurantDao, restaurantListDataStore)
    }

    @Provides
    @Singleton
    fun provideLikeListRepository(
        likeRestaurantDao: LikeRestaurantDao
    ): LikeRestaurantRepository = LikeRestaurantRepositoryImpl(likeRestaurantDao)

    @Provides
    @Singleton
    fun provideLocationService(
        @ApplicationContext context: Context
    ): FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    @Provides
    @Singleton
    fun provideSettingClient(
        @ApplicationContext context: Context
    ): SettingsClient = LocationServices.getSettingsClient(context)
}
