package jp.dosukoityanko.ui.view.util

import android.view.View
import androidx.databinding.BindingAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior

@BindingAdapter("isVisibleBottomSheet")
fun View.toggleBottomSheet(isVisibleBottomSheet: Boolean?) {
    BottomSheetBehavior.from(this).apply {
        state = if (isVisibleBottomSheet == true) {
            BottomSheetBehavior.STATE_EXPANDED
        } else {
            BottomSheetBehavior.STATE_HIDDEN
        }
    }
}
