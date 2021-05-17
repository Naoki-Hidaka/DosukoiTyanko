package jp.dosukoityanko.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jp.dosukoityanko.AppDatabase
import jp.dosukoityanko.BuildConfig
import jp.dosukoityanko.data.api.IApiType
import jp.dosukoityanko.data.entity.common.JsonHandler
import jp.dosukoityanko.data.repository.likeList.LikeRestaurantDao
import jp.dosukoityanko.data.repository.likeList.LikeRestaurantRepository
import jp.dosukoityanko.data.repository.likeList.LikeRestaurantRepositoryImpl
import jp.dosukoityanko.data.repository.restaurantList.RestaurantListDataStore
import jp.dosukoityanko.data.repository.restaurantList.RestaurantListDataStoreImpl
import jp.dosukoityanko.data.repository.restaurantList.RestaurantListRepository
import jp.dosukoityanko.data.repository.restaurantList.RestaurantListRepositoryImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
    fun provideApiType(): IApiType {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(
                        HttpLoggingInterceptor().apply {
                            level = HttpLoggingInterceptor.Level.BODY
                        }
                    )
                    .build()
            )
            .addConverterFactory(JsonHandler.converter.run { GsonConverterFactory.create(this) })
            .build()
            .create(IApiType::class.java)
    }

    @Provides
    @Singleton
    fun provideApiKey(): String {
        return BuildConfig.API_KEY
    }

    @Provides
    @Singleton
    fun provideLikeDao(db: AppDatabase) = db.likeRestaurantDao()

    @Provides
    @Singleton
    fun provideRestaurantListDataStore(
        apiType: IApiType,
        apiKey: String
    ): RestaurantListDataStore = RestaurantListDataStoreImpl(apiType, apiKey)

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
}
