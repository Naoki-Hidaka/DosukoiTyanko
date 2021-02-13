package jp.dosukoityanko.domain.entity.restaurantList

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class RestaurantTest {

    private lateinit var restaurant: Restaurant
    private lateinit var emptyBudgetRestaurant: Restaurant

    @Before
    fun setUp() {
        restaurant = mockRestaurant()
        emptyBudgetRestaurant = mockEntityBudgetRestaurant()

    }

    @Test
    fun formatBudget() {
        assertThat(restaurant.formatBudget()).isEqualTo("${restaurant.budget}円")
        assertThat(emptyBudgetRestaurant.formatBudget()).isEqualTo("- 円")
    }

    @Test
    fun budgetInt() {
        assertThat(restaurant.budgetInt()).isEqualTo(restaurant.budget?.toInt())
        assertThat(emptyBudgetRestaurant.budgetInt()).isEqualTo(0)

    }

    private fun mockRestaurant(): Restaurant {
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

    private fun mockEntityBudgetRestaurant(): Restaurant {
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
            "",
            "1",
            "1",
            "1"
        )
    }
}