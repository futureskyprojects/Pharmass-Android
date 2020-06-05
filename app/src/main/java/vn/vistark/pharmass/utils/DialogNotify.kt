package vn.vistark.pharmass.utils

import android.content.Context
import cn.pedant.SweetAlert.SweetAlertDialog
import vn.vistark.pharmass.R
import java.util.*

class DialogNotify {
    companion object {
        fun missingInput(context: Context, msg: String) {
            SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE).apply {
                titleText = msg
                contentText =
                    context.getString(R.string.nhap_thieu).toUpperCase(Locale.getDefault())
                show()
            }
        }

        fun error(context: Context, msg: String) {
            SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE).apply {
                titleText = msg
                contentText =
                    context.getString(R.string.loi).toUpperCase(Locale.getDefault())
                show()
            }
        }

        fun warning(context: Context, msg: String) {
            SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE).apply {
                titleText = msg
                contentText =
                    context.getString(R.string.chu_y).toUpperCase(Locale.getDefault())
                show()
            }
        }

        fun success(context: Context, msg: String) {
            SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE).apply {
                titleText = msg
                contentText =
                    context.getString(R.string.hoan_tat).toUpperCase(Locale.getDefault())
                show()
            }
        }
    }

    class loading(val context: Context) {
        var sweetAlertDialog: SweetAlertDialog? = null
        fun show(msg: String = "Vui lòng đợi trong giây lát") {
            sweetAlertDialog?.dismiss()
            sweetAlertDialog = SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
            sweetAlertDialog?.apply {
                titleText = msg
                contentText = context.getString(R.string.dang_xu_ly)
                setCancelable(false)
                showCancelButton(false)
                show()
            }
        }

        fun close() {
            sweetAlertDialog?.dismiss()
        }
    }
}