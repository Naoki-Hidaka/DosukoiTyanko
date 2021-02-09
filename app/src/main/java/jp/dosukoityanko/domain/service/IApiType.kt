package jp.dosukoityanko.domain.service

import jp.dosukoityanko.domain.entity.common.ApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface IApiType {

    @GET(".")
    suspend fun getRestaurant(
        @Query("keyid") keyId: String,
        @Query("buffet") buffet: Int = 1,
        @Query("hit_per_page") hitPerPage: Int = 40
    ): Response<ApiResponse>

    @GET(".")
    suspend fun getRestaurant(
        @Query("keyid") keyId: String,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("buffet") buffet: Int = 1,
        @Query("range") range: Int = 3,
        @Query("hit_per_page") hitPerPage: Int = 40
    ): Response<ApiResponse>
}
