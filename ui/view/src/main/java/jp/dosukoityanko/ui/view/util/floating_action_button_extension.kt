package jp.dosukoityanko.ui.view.util

import androidx.databinding.BindingAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

@BindingAdapter("isVisibleFab")
fun FloatingActionButton.toggle(isVisibleFab: Boolean?) {
    if (isVisibleFab == true) show()
    else hide()
}
