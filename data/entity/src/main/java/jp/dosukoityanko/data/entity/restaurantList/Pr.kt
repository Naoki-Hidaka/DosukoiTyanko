package jp.dosukoityanko.data.entity.restaurantList

import androidx.room.ColumnInfo

data class Pr(
    @ColumnInfo(name = "prShort")
    val prShort: String,
    val prLong: String
)
