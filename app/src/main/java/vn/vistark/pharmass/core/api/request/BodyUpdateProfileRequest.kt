package vn.vistark.pharmass.core.api.request


import androidx.databinding.BaseObservable
import com.google.gson.annotations.SerializedName
import vn.vistark.pharmass.core.model.User
import vn.vistark.pharmass.core.model.UserAddress

data class BodyUpdateProfileRequest(
    @SerializedName("fullName")
    var fullName: String = "",
    @SerializedName("birthDay")
    var birthDay: String? = "",
    @SerializedName("address")
    var address: UserAddress = UserAddress(),
    @SerializedName("introduction")
    var introduction: String = "",
    @SerializedName("gender")
    var gender: Int = -1,
    @SerializedName("identifyCardNumber")
    var identifyCardNumber: String = "",
    @SerializedName("phoneNumber")
    var phoneNumber: String = "",
    @SerializedName("avatar")
    var avatar: String = ""
) : BaseObservable() {
    companion object {
        fun copy(user: User): BodyUpdateProfileRequest {
            return BodyUpdateProfileRequest(
                user.fullName,
                user.birthDay,
                user.address,
                user.introduction,
                user.gender,
                user.identifyCardNumber,
                user.phoneNumber,
                user.avatar ?: ""
            )
        }
    }
}