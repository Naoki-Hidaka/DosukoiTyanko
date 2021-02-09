package jp.dosukoityanko.presentation.view.util

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
        .show()
}

fun confirmDialog(
    context: Context,
    title: String? = null,
    message: String? = null,
    decisionAction: () -> Unit,
) {
    AlertDialog.Builder(context)
        .apply { title?.let { setTitle(it) } }
        .apply { message?.let { setMessage(it) } }
        .setPositiveButton("OK") { dialog, _ ->
            decisionAction.invoke()
            dialog.dismiss()
        }
        .setNegativeButton("キャンセル") { dialog, _ ->
            dialog.dismiss()
        }
        .show()
}
