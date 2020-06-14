package vn.vistark.pharmass.core.model

import com.google.gson.annotations.SerializedName
import vn.vistark.pharmass.core.api.response.PharmacySimpleOwner

data class GoodsCategory(
    var id: Int = -1,
    var name: String = "",
    var description: String = "",
    var pharmacy: PharmacySimpleOwner,
    @SerializedName("created_at")
    var createdAt: String = "",
    @SerializedName("updated_at")
    var updatedAt: String = ""
) {
}