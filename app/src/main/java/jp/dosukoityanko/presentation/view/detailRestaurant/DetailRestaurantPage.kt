package jp.dosukoityanko.presentation.view.detailRestaurant

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import jp.dosukoityanko.domain.entity.restaurantList.Restaurant
import jp.dosukoityanko.presentation.viewmodel.restaurantList.RestaurantListViewModel

@Composable
fun DetailRestaurantPage(viewModel: RestaurantListViewModel) {
    Box(modifier = Modifier.fillMaxSize()) {
        DetailRestaurantBrowser(
            viewModel.selectedRestaurant.observeAsState().value
        ) {
            viewModel.isLoading.value = false
        }
        ProgressIndicator(viewModel.isLoading.observeAsState(true).value)
    }
}

@Composable
fun ProgressIndicator(isLoading: Boolean) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        if (isLoading) {
            CircularProgressIndicator(progress = 1.0F, modifier = Modifier.wrapContentSize())
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
            })
    }
}

@Preview
@Composable
fun PreviewDetailRestaurantPage() {
    DetailRestaurantPage(viewModel = viewModel())
}

@Preview
@Composable
fun PreviewProgressBar() {
    ProgressIndicator(isLoading = true)
}

@Preview
@Composable
fun PreviewDetailRestaurantBrowser() {
    DetailRestaurantBrowser(selectedRestaurant = null) {}
}