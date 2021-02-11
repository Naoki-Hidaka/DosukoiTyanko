package jp.dosukoityanko.domain.repository.restaurantList

import android.location.Location
import jp.dosukoityanko.domain.entity.common.Resource
import jp.dosukoityanko.domain.entity.restaurantList.Restaurant
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object FakeRestaurantListDataStore : RestaurantListDataStore {

    override fun fetchRestaurants(
        location: Location?,
        operation: (List<Restaurant>) -> List<Restaurant>
    ): Flow<Resource<List<Restaurant>>> = flow {
        emit(Resource.InProgress)
        delay(1000)
    }
}
