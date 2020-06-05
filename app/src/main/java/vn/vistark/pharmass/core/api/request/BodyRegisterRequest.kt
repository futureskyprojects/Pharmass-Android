package vn.vistark.pharmass.core.api.request


import androidx.databinding.BaseObservable
import com.google.gson.annotations.SerializedName

data class BodyRegisterRequest(
    @SerializedName("fullName")
    var fullName: String = "",
    @SerializedName("username")
    var username: String = "",
    @SerializedName("email")
    var email: String = "",
    @SerializedName("password")
    var password: String = "",
    @SerializedName("phoneNumber")
    var phoneNumber: String = ""
) : BaseObservable() {
    var rePassword: String = ""

    fun validate(): String {
        if (fullName.isEmpty())
            return "Bạn chưa cung cấp họ và tên"
        else if (username.isEmpty())
            return "Vui lòng nhập tên tài khoản của bạn"
        else if (email.isEmpty())
            return "Vui lòng cung cấp địa chỉ email của bạn"
        else if (password.isEmpty())
            return "Hãy nhập mật khẩu cho tài khoản"
        else if (rePassword.isEmpty())
            return "Vui lòng nhập lại mật khẩu của bạn để đảm bảo bạn nhập đúng"
        else if (phoneNumber.isEmpty())
            return "Bạn còn thiếu số điện thoại"
        else if (username.length < 5)
            return "Tên tài khoản tối thiểu là 5 ký tự"
        else if (password.length < 8)
            return "Mật khẩu tối thiểu là 8 ký tự"
        else if (phoneNumber.length != 10)
            return "Số điện thoại hiện tại phải đủ 10 số"
        else if (rePassword != password)
            return "Mật khẩu nhập lại không khớp với mật khẩu bạn đã nhập"
        else
            return ""
    }
}