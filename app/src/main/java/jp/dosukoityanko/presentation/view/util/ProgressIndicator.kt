package jp.dosukoityanko.presentation.view.util

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import jp.dosukoityanko.R

@Composable
fun ProgressIndicator(isLoading: Boolean?) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        if (isLoading == true) {
            CircularProgressIndicator(
                modifier = Modifier.wrapContentSize(),
                color = colorResource(R.color.orange)
            )
        }
    }
}
