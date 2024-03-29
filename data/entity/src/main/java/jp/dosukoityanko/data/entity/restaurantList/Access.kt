package jp.dosukoityanko.data.entity.restaurantList

import androidx.room.ColumnInfo

data class Access(
    @ColumnInfo(name = "line")
    val line: String,
    val station: String,
    val stationExit: String,
    val walk: String,
    val note: String
)
