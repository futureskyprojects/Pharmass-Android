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
import vn.vistark.pharmass.core.model.PharmacyStaff
import vn.vistark.pharmass.utils.DialogNotify
import java.lang.Exception

class GetPharmacyGoodsInCategoryProcessing(
    context: Context,
    pharmacyId: Int,
    goodsCategoryId: Int
) {
    var onFinished: ((List<Goods>?) -> Unit)? = null

    init {
        APIUtils.mAPIServices?.getPharmacyGoodsInCategory(goodsCategoryId, pharmacyId)
            ?.enqueue(object : Callback<List<Goods>> {
                override fun onFailure(call: Call<List<Goods>>, t: Throwable) {
                    DialogNotify.error(
                        context,
                        t.message ?: "Lỗi không xác định khi lấy danh sách hàng theo mục đã chọn"
                    )
                }

                override fun onResponse(
                    call: Call<List<Goods>>,
                    response: Response<List<Goods>>
                ) {
                    if (response.isSuccessful) {
                        // Khi thực hiện thành công
                        onFinished?.invoke(response.body())
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
                                    DialogNotify.error(context, extractError)
                                    onFinished?.invoke(null)
                                    return
                                }
                            }
                        } catch (e: Exception) {
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
                            DialogNotify.error(context, error401Response.message)
                        }
                        onFinished?.invoke(null)
                        return
                    }
                    // Nếu lỗi không nằm trong dự tính
                    DialogNotify.error(context, response.message())
                    onFinished?.invoke(null)
                }
            })
    }
}