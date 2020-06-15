package vn.vistark.pharmass.core.api.response

import com.google.gson.annotations.SerializedName
import vn.vistark.pharmass.core.api.response.PharmacySimpleOwner

data class GoodsCategorySimplePharmacy(
    var id: Int = -1,
    var name: String = "",
    var description: String = "",
    var pharmacy: Int = -1,
    @SerializedName("created_at")
    var createdAt: String = "",
    @SerializedName("updated_at")
    var updatedAt: String = ""
) {
}