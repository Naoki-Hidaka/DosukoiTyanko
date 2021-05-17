package jp.dosukoityanko.ui.view.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("imageUrl")
fun ImageView.setImageUrl(imageUrl: String?) {
    imageUrl ?: return
    Glide.with(context).load(imageUrl).into(this)
}
