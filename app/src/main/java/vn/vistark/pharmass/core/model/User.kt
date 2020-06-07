package vn.vistark.pharmass.core.model


import com.google.gson.annotations.SerializedName
import vn.vistark.pharmass.core.api.APIUtils
import vn.vistark.pharmass.core.api.RetrofitClient
import vn.vistark.pharmass.core.constants.Constants
import vn.vistark.pharmass.utils.GlideUtils
import vn.vistark.pharmass.utils.RegexLibs
import vn.vistark.pharmass.utils.UrlUtils

data class User(
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("username")
    var username: String = "",
    @SerializedName("email")
    var email: String = "",
    @SerializedName("provider")
    var provider: String = "",
    @SerializedName("confirmed")
    var confirmed: Boolean = false,
    @SerializedName("blocked")
    var blocked: Boolean = false,
    @SerializedName("created_at")
    var createdAt: String = "",
    @SerializedName("updated_at")
    var updatedAt: String = "",
    @SerializedName("fullName")
    var fullName: String = "",
    @SerializedName("birthDay")
    var birthDay: String = "",
    @SerializedName("address")
    var address: UserAddress = UserAddress(),
    @SerializedName("coordinates")
    var coordinates: Coordinates = Coordinates(),
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
) {
    fun getAvatarFullAddress(): String {
        if (avatar.isEmpty())
            return ""
        if (avatar.contains(RegexLibs.url.toRegex()))
            return avatar
        else
            return UrlUtils.standard("${RetrofitClient.BASE_URL}/$avatar")
    }
}