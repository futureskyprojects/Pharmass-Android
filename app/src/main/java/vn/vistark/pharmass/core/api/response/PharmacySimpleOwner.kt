package vn.vistark.pharmass.core.api.response


import androidx.databinding.BaseObservable
import com.google.gson.annotations.SerializedName
import vn.vistark.pharmass.core.api.RetrofitClient
import vn.vistark.pharmass.core.model.Coordinates
import vn.vistark.pharmass.core.model.User
import vn.vistark.pharmass.core.model.UserAddress
import vn.vistark.pharmass.utils.RegexLibs
import vn.vistark.pharmass.utils.UrlUtils

data class PharmacySimpleOwner(
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("logo")
    var logo: String = "",
    @SerializedName("featureImages")
    var featureImages: String = "",
    @SerializedName("name")
    var name: String = "",
    @SerializedName("shortDescription")
    var shortDescription: String = "",
    @SerializedName("introduction")
    var introduction: String = "",
    @SerializedName("address")
    var address: UserAddress = UserAddress(),
    @SerializedName("coordinates")
    var coordinates: Coordinates = Coordinates(),
    @SerializedName("confirmed")
    var confirmed: Boolean = false,
    @SerializedName("blocked")
    var blocked: Boolean = false,
    @SerializedName("openTime")
    var openTime: String = "",
    @SerializedName("closeTime")
    var closeTime: String = "",
    @SerializedName("created_at")
    var createdAt: String = "",
    @SerializedName("updated_at")
    var updatedAt: String = "",
    @SerializedName("dayInterval")
    var dayInterval: String = "",
    @SerializedName("owner")
    var users: Int = -1
) : BaseObservable() {
    fun getFeatureImageFullAddress(): String {
        if (featureImages.isEmpty())
            return ""
        if (featureImages.contains(RegexLibs.url.toRegex()))
            return featureImages
        else
            return UrlUtils.standard("${RetrofitClient.BASE_URL}/$featureImages")
    }

    fun getLogoImageFullAddress(): String {
        if (logo.isEmpty())
            return ""
        if (logo.contains(RegexLibs.url.toRegex()))
            return logo
        else
            return UrlUtils.standard("${RetrofitClient.BASE_URL}/$logo")
    }
}