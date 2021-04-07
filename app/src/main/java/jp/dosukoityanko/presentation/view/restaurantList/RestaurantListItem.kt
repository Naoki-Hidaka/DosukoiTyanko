package jp.dosukoityanko.presentation.view.restaurantList

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import jp.dosukoityanko.domain.entity.restaurantList.Restaurant

@Composable
fun RestaurantListItem(restaurant: Restaurant) {
    Row(modifier = Modifier.fillMaxSize()) {
        RestaurantImage(imageUrl = restaurant.imageUrl?.shopImage1)
    }
}

@Composable
fun RestaurantImage(imageUrl: String?) {
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    Glide.with(LocalContext.current).asBitmap().load(imageUrl)
        .into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                bitmap = resource
                //Image(bitmap = resource.asImageBitmap(), contentDescription = "")
            }

            override fun onLoadCleared(placeholder: Drawable?) {
                TODO("Not yet implemented")
            }
        })
    Image(bitmap = bitmap!!.asImageBitmap(), contentDescription = "")
}
