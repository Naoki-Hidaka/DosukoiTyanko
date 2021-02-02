package com.example.myapplication.dosukoityanko.domain.entity.restaurantList

import androidx.room.ColumnInfo

data class ImageUrl(
    @ColumnInfo(name = "shopImage1")
    val shopImage1: String,
    val shopImage2: String,
    val qrcode: String
)
