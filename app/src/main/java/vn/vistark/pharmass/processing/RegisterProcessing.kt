package vn.vistark.pharmass.processing

import android.content.Context
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.vistark.pharmass.core.api.APIUtils
import vn.vistark.pharmass.core.api.ErrorLibrary
import vn.vistark.pharmass.core.api.request.BodyRegisterRequest
import vn.vistark.pharmass.core.api.response.BodyAuthenticationResponse
import vn.vistark.pharmass.core.api.response.Error400Response
import vn.vistark.pharmass.utils.DialogNotify

class RegisterProcessing(context: Context, bodyRegisterRequest: BodyRegisterRequest) {
    var onFinished: ((BodyAuthenticationResponse?) -> Unit)? = null

    init {
        val loading = DialogNotify.loading(context)
        loading.show("Đang xử lý")
        APIUtils.mAPIServices?.register(bodyRegisterRequest)
            ?.enqueue(object : Callback<BodyAuthenticationResponse> {
                override fun onFailure(call: Call<BodyAuthenticationResponse>, t: Throwable) {
                    loading.close()
                    DialogNotify.error(context, t.message ?: "Lỗi không xác định")
                }

                override fun onResponse(
                    call: Call<BodyAuthenticationResponse>,
                    response: Response<BodyAuthenticationResponse>
                ) {
                    loading.close()
                    println("Nội dung lỗi - ĐĂNG KÝ: " + response.errorBody()?.string() + " >>>>>>")
                    if (response.isSuccessful) {
                        // Khi thực hiện thành công
                        onFinished?.invoke(response.body())
                        return
                    } else if (response.code() == 400) {
                        // Khi nhận được thông báo lỗi 400
                        val error400 = Gson().fromJson(
                            response.errorBody()?.string(),
                            Error400Response::class.java
                        )
                        if (error400 != null) {
                            val extractError = ErrorLibrary.find(
                                error400.message.firstOrNull()?.messages?.firstOrNull()?.id ?: ""
                            )
                            if (extractError.isNotEmpty()) {
                                DialogNotify.error(context, extractError)
                                return
                            }
                        }
                    }
                    // Nếu lỗi không nằm trong dự tính
                    DialogNotify.error(context, response.message())
                }
            })
    }
}