package jp.dosukoityanko.presentation.view.util

import android.view.View
import androidx.databinding.BindingAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import timber.log.Timber

@BindingAdapter("isVisibleBottomSheet")
fun View.toggleBottomSheet(isVisibleBottomSheet: Boolean?) {
    Timber.d("debug: state $isVisibleBottomSheet")
    BottomSheetBehavior.from(this).apply {
        state = if (isVisibleBottomSheet == true) {
            BottomSheetBehavior.STATE_EXPANDED
        } else {
            BottomSheetBehavior.STATE_HIDDEN
        }
    }
}
