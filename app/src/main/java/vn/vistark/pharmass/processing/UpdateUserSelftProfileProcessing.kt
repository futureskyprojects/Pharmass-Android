package vn.vistark.pharmass.processing

import android.content.Context
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.vistark.pharmass.core.api.APIUtils
import vn.vistark.pharmass.core.api.ErrorLibrary
import vn.vistark.pharmass.core.api.request.BodyLoginRequest
import vn.vistark.pharmass.core.api.request.BodyRegisterRequest
import vn.vistark.pharmass.core.api.request.BodyUpdateProfileRequest
import vn.vistark.pharmass.core.api.response.BodyAuthenticationResponse
import vn.vistark.pharmass.core.api.response.Error400Response
import vn.vistark.pharmass.core.api.response.Error401Response
import vn.vistark.pharmass.core.constants.Constants
import vn.vistark.pharmass.core.model.User
import vn.vistark.pharmass.utils.DialogNotify

class UpdateUserSelftProfileProcessing(
    context: Context,
    bodyUpdateProfileRequest: BodyUpdateProfileRequest
) {
    var onFinished: ((User?) -> Unit)? = null

    init {
        val loading = DialogNotify.loading(context)
        loading.show("Đang cập nhật hồ sơ")
        APIUtils.mAPIServices?.updateUserSelft(bodyUpdateProfileRequest)
            ?.enqueue(object : Callback<User> {
                override fun onFailure(call: Call<User>, t: Throwable) {
                    loading.close()
                    DialogNotify.error(context, t.message ?: "Lỗi không xác định")
                }

                override fun onResponse(
                    call: Call<User>,
                    response: Response<User>
                ) {
                    loading.close()
                    if (response.code() == 401) {
                        Constants.token = "" // Xóa token ngay
                        val error401Response = Gson().fromJson(
                            response.errorBody()?.string(),
                            Error401Response::class.java
                        )
                        if (error401Response != null) {
                            DialogNotify.error(context, error401Response.message)
                        }
                        return
                    }

                    println("UPDATE PROFILE API - SUCCESS: " + Gson().toJson(response.body()))
                    println("UPDATE PROFILE API - FAIL: " + Gson().toJson(response.errorBody()))
                    onFinished?.invoke(response.body())
                }
            })
    }
}