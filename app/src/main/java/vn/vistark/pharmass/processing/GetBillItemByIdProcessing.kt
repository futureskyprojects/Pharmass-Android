package vn.vistark.pharmass.processing

import android.content.Context
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.vistark.pharmass.core.api.APIUtils
import vn.vistark.pharmass.core.api.ErrorLibrary
import vn.vistark.pharmass.core.api.response.Error400Response
import vn.vistark.pharmass.core.api.response.Error401Response
import vn.vistark.pharmass.core.constants.Constants
import vn.vistark.pharmass.core.model.BillItem
import vn.vistark.pharmass.core.model.SimpleBillItem
import vn.vistark.pharmass.utils.DialogNotify
import java.lang.Exception

class GetBillItemByIdProcessing(
    context: Context,
    id: Int,
    hideNotify: Boolean = false
) {
    var onFinished: ((BillItem?) -> Unit)? = null

    init {
        APIUtils.mAPIServices?.getBillItemById(id)
            ?.enqueue(object : Callback<List<BillItem>> {
                override fun onFailure(call: Call<List<BillItem>>, t: Throwable) {
                    if (!hideNotify)
                        DialogNotify.error(
                            context,
                            t.message ?: "Lỗi không xác định khi lấy sản phẩm của đơn bán hiện tại"
                        )
                }

                override fun onResponse(
                    call: Call<List<BillItem>>,
                    response: Response<List<BillItem>>
                ) {
                    if (response.isSuccessful) {
                        // Khi thực hiện thành công
                        onFinished?.invoke(response.body()?.first())
                        return
                    } else if (response.code() == 400) {
                        try {
                            // Khi nhận được thông báo lỗi 400
                            val error400 = Gson()
                                .fromJson(
                                    response.errorBody()?.string(),
                                    Error400Response::class.java
                                )
                            if (error400 != null) {
                                val extractError = ErrorLibrary.find(
                                    error400.message.firstOrNull()?.messages?.firstOrNull()?.id
                                        ?: ""
                                )
                                if (extractError.isNotEmpty()) {
                                    if (!hideNotify)
                                        DialogNotify.error(context, extractError)
                                    onFinished?.invoke(null)
                                    return
                                }
                            }
                        } catch (e: Exception) {
                            if (!hideNotify)
                                DialogNotify.error(
                                    context,
                                    "Không lấy được dữ liệu"
                                )
                        }
                    } else if (response.code() == 401) {
                        Constants.token = "" // Xóa token ngay
                        val error401Response = Gson().fromJson(
                            response.errorBody()?.string(),
                            Error401Response::class.java
                        )
                        if (error401Response != null) {
                            if (!hideNotify)
                                DialogNotify.error(context, error401Response.message)
                        }
                        onFinished?.invoke(null)
                        return
                    }
                    // Nếu lỗi không nằm trong dự tính
                    if (!hideNotify)
                        DialogNotify.error(context, response.message())
                    onFinished?.invoke(null)
                }
            })
    }
}