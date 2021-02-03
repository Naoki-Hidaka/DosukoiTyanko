package com.example.myapplication.dosukoityanko.presentation.view.util

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("isVisible")
fun View.toggle(isVisible: Boolean?) {
    isVisible ?: return
    visibility = if (isVisible) View.VISIBLE else View.GONE
}
