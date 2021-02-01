package com.example.myapplication.dosukoityanko.domain.entity.restaurantList

import androidx.room.ColumnInfo

data class Pr(
    @ColumnInfo(name = "prShort")
    val prShort: String,
    val prLong: String
)
