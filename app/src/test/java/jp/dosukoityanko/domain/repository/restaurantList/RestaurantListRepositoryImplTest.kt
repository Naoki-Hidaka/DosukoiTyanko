package jp.dosukoityanko.domain.repository.restaurantList

import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import jp.dosukoityanko.domain.entity.common.Resource
import jp.dosukoityanko.domain.entity.createMockRestaurantList
import jp.dosukoityanko.domain.repository.likeList.LikeRestaurantDao
import kotlinx.coroutines.flow.dropWhile
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

class RestaurantListRepositoryImplTest {

    private lateinit var restaurantRepository: RestaurantListRepository
    private lateinit var restaurantListDataStore: RestaurantListDataStore
    private lateinit var likeRestaurantDao: LikeRestaurantDao

    private val restaurantList = createMockRestaurantList()


    @Before
    fun setUp() {
        likeRestaurantDao = mockk()
        restaurantListDataStore = FakeRestaurantListDataStore(restaurantList)
        restaurantRepository =
            RestaurantListRepositoryImpl(likeRestaurantDao, restaurantListDataStore)
    }

    @Test
    fun getRestaurantBelowThreeThousand() {
        runBlockingTest {
            val flow = restaurantRepository.getRestaurantBelowThreeThousand(null)
            val flowValue = flow.dropWhile { it is Resource.InProgress }.first()
            assertThat(flowValue.extractData?.all { it.budget?.toInt() ?: 0 <= 3000 }).isTrue()

        }
    }

    @Test
    fun getRestaurantBelowFiveThousand() {
        runBlockingTest {
            val flow = restaurantRepository.getRestaurantBelowThreeThousand(null)
            val flowValue = flow.dropWhile { it is Resource.InProgress }.first()
            assertThat(flowValue.extractData?.all { it.budget?.toInt() ?: 0 <= 5000 }).isTrue()
        }
    }
}