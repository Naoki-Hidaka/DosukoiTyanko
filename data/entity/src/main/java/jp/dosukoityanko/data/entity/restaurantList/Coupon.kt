package jp.dosukoityanko.data.entity.restaurantList

import androidx.room.ColumnInfo

data class Coupon(
    @ColumnInfo(name = "pc")
    val pc: String,
    val mobile: String
)
