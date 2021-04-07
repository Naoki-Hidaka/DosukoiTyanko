package jp.dosukoityanko.presentation.view.restaurantList

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import jp.dosukoityanko.R

@Composable
fun EmptyState(isVisible: Boolean?) {
    if (isVisible == true) Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.tyanko),
            contentDescription = null
        )
    }
}
