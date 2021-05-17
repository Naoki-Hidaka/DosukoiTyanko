package jp.dosukoityanko.data.entity.restaurantList

data class ApiResponse(
    val totalHitCount: Int,
    val hitPerPage: Int,
    val pageOffset: Int,
    val rest: List<Restaurant>
)
