package jp.dosukoityanko.domain.service

import jp.dosukoityanko.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    const val apiKey = BuildConfig.API_KEY

    private const val baseUrl = BuildConfig.BASE_URL

    private val jsonConverter by lazy {
        JsonHandler.converter.run { GsonConverterFactory.create(this) }
    }

    private val httpLoggingInterceptor by lazy {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    val retrofit: IApiType by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(jsonConverter)
            .build()
            .create(IApiType::class.java)
    }
}
