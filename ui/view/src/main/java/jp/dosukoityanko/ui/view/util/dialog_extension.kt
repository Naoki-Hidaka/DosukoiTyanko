package jp.dosukoityanko.ui.view.util

import android.app.AlertDialog
import android.content.Context

fun showRetryDialog(
    context: Context,
    retryAction: (() -> Unit)?,
    title: String? = "エラー",
    message: String? = "エラーが発生しました",
) {
    AlertDialog.Builder(context)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton("リトライ") { dialog, _ ->
            retryAction?.invoke()
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
