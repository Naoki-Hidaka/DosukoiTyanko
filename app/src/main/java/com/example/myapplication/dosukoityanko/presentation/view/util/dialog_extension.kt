package com.example.myapplication.dosukoityanko.presentation.view.util

import android.app.AlertDialog
import android.content.Context

fun showRetryDialog(
    context: Context,
    retryAction: () -> Unit
) {
    AlertDialog.Builder(context)
        .setTitle("エラー")
        .setMessage("エラーが発生しました")
        .setPositiveButton("リトライ") { dialog, _ ->
            retryAction.invoke()
            dialog.dismiss()
        }
        .setNegativeButton("キャンセル") { dialog, _ ->
            dialog.dismiss()
        }
}
