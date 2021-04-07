package jp.dosukoityanko.presentation.view.likeList

import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.viewinterop.AndroidView
import jp.dosukoityanko.R
import jp.dosukoityanko.domain.entity.restaurantList.Restaurant
import jp.dosukoityanko.presentation.viewmodel.likeList.LikeListViewModel

@Composable
fun LikeDetailPage(viewModel: LikeListViewModel) {
    Box(modifier = Modifier.fillMaxSize()) {
        DetailRestaurantBrowser(
            viewModel.selectedRestaurant.observeAsState().value
        ) {
            viewModel.isLoading.value = false
        }
        ProgressIndicator(viewModel.isLoading.observeAsState().value)
    }
}

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

@Composable
fun DetailRestaurantBrowser(
    selectedRestaurant: Restaurant?,
    onPageFinished: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                WebView(context).apply {
                    selectedRestaurant?.urlMobile?.let {
                        loadUrl(it)
                    }
                    settings.apply {
                        supportZoom()
                        builtInZoomControls = true
                    }
                    webViewClient = object : WebViewClient() {
                        override fun onPageFinished(view: WebView?, url: String?) {
                            onPageFinished()
                        }
                    }
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }
            }
        )
    }
}
