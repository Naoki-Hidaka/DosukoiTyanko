package jp.dosukoityanko.domain.entity.common

import jp.dosukoityanko.domain.entity.restaurantList.Restaurant

data class ApiResponse(
    val totalHitCount: Int,
    val hitPerPage: Int,
    val pageOffset: Int,
    val rest: List<Restaurant>
)
