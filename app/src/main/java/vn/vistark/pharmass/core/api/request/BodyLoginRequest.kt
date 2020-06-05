package vn.vistark.pharmass.core.api.request


import androidx.databinding.BaseObservable
import com.google.gson.annotations.SerializedName

data class BodyLoginRequest(
    @SerializedName("identifier")
    var identifier: String = "",
    @SerializedName("password")
    var password: String = ""
) : BaseObservable() {
    fun validate(): String {
        if (identifier.isEmpty())
            return "Vui lòng nhập tên tài khoản hoặc email của bạn"
        else if (identifier.length < 5)
            return "Tên tài khoản hoặc email không đúng"
        else if (password.isEmpty())
            return "Bạn chưa nhập mật khẩu"
        else if (password.length < 8)
            return "Mật khẩu có độ dài không hợp lệ"
        else
            return ""
    }
}