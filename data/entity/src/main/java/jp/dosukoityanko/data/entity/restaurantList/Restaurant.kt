package jp.dosukoityanko.data.entity.restaurantList

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Restaurant(
    @PrimaryKey
    val id: String,
    val updateDate: String?,
    val name: String?,
    val nameKana: String?,
    val latitude: String?,
    val longitude: String?,
    val category: String?,
    val url: String?,
    @Deprecated("Use url instead of urlMobile")
    val urlMobile: String?,
    @Embedded val couponUrl: Coupon?,
    @Embedded val imageUrl: ImageUrl?,
    val address: String?,
    val tel: String?,
    val telSub: String?,
    val openTime: String?,
    val holiday: String?,
    @Embedded val access: Access?,
    val parkingLots: String?,
    @Embedded val pr: Pr?,
    val budget: String?,
    val party: String?,
    val lunch: String?,
    val creditCard: String?
) {
    fun formatBudget(): String {
        return if (budget?.isNotEmpty() == true) "${budget}円" else "- 円"
    }

    fun budgetInt(): Int {
        return if (budget?.isEmpty() == true) 0 else budget?.toInt() ?: 0
    }
}
