package jp.dosukoityanko.domain.entity

import jp.dosukoityanko.domain.entity.restaurantList.*

fun createMockRestaurantList(): List<Restaurant> {
    val mutableList = mutableListOf<Restaurant>()
    repeat(100) {
        mutableList.add(createMockRestaurant())
    }

    return mutableList.toList()
}

fun createMockRestaurant(): Restaurant {
    return Restaurant(
        "11223344",
        "2021-02-11",
        "テストレストラン",
        "テストレストラン",
        "35.000000",
        "126.000000",
        "焼肉",
        "https://example.com/",
        "https://example.com/",
        Coupon(
            "https://example.com/",
            "https://example.com/"
        ),
        ImageUrl(
            "https://placehold.jp/150x150.png",
            "https://placehold.jp/150x150.png",
            "https://placehold.jp/150x150.png"
        ),
        "東京都渋谷区11-11-11-11-11",
        "00000000000",
        "00000000000",
        "11:00-22:00",
        "無",
        Access(
            "JR山手線",
            "新宿駅",
            "西口",
            "5",
            "無"
        ),
        "無",
        Pr(
            "美味しいよ",
            "美味しいよ"
        ),
        (0..5000).random().toString(),
        "1",
        "1",
        "1"
    )
}
