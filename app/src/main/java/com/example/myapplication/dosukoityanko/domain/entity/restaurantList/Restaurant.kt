package com.example.myapplication.dosukoityanko.domain.entity.restaurantList

data class Restaurant(
    val id: String,
    val updateDate: String,
    val name: String,
    val nameKana: String,
    val latitude: String,
    val longitude: String,
    val category: String,
    val url: String,
    val urlMobile: String,
    val couponUrl: Coupon,
    val imageUrl: ImageUrl,
    val address: String,
    val tel: String,
    val telSub: String,
    val fax: String,
    val openTime: String,
    val holiday: String,
    val access: Access,
    val parkingLots: String,
    val pr: Pr,
    val budget: Int,
    val party: Int,
    val lunch: Int,
    val creditCard: String

) {
}