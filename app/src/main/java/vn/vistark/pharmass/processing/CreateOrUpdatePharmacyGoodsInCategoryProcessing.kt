package vn.vistark.pharmass.processing

import android.content.Context
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.vistark.pharmass.core.api.APIUtils
import vn.vistark.pharmass.core.api.ErrorLibrary
import vn.vistark.pharmass.core.api.request.BodyCreatePharmacyRequest
import vn.vistark.pharmass.core.model.Pharmacy
import vn.vistark.pharmass.core.api.response.Error400Response
import vn.vistark.pharmass.core.api.response.Error401Response
import vn.vistark.pharmass.core.constants.Constants
import vn.vistark.pharmass.core.model.Goods
import vn.vistark.pharmass.utils.DialogNotify

class CreateOrUpdatePharmacyGoodsInCategoryProcessing(
    context: Context,
    goods: Goods,
    hideNotify: Boolean = false
) {
    var onFinished: ((Goods?) -> Unit)? = null

    init {
        val loading = DialogNotify.loading(context)
        if (!hideNotify)
            loading.show("Đang tiến hành cập nhật sản phẩm")
        APIUtils.mAPIServices?.run {
            if (goods.id > 0) {
                updatePharmacyGoodsInCategory(goods.id, goods)
            } else {
                createPharmacyGoodsInCategory(goods)
            }
        }
            ?.enqueue(object : Callback<Goods> {
                override fun onFailure(call: Call<Goods>, t: Throwable) {
                    loading.close()
                    if (!hideNotify)
                        DialogNotify.error(context, t.message ?: "Lỗi không xác định")
                }

                override fun onResponse(
                    call: Call<Goods>,
                    response: Response<Goods>
                ) {
                    loading.close()
                    if (response.isSuccessful) {
                        // Khi thực hiện thành công
                        onFinished?.invoke(response.body())
                        return
                    } else if (response.code() == 400) {
                        // Khi nhận được thông báo lỗi 400
                        val error400 = Gson()
                            .fromJson(response.errorBody()?.string(), Error400Response::class.java)
                        if (error400 != null) {
                            val extractError = ErrorLibrary.find(
                                error400.message.firstOrNull()?.messages?.firstOrNull()?.id ?: ""
                            )
                            if (extractError.isNotEmpty()) {
                                if (!hideNotify)
                                    DialogNotify.error(context, extractError)
                                return
                            }
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
                        return
                    }
                    // Nếu lỗi không nằm trong dự tính
                    if (!hideNotify)
                        DialogNotify.error(context, response.message())
                }
            })
    }
}