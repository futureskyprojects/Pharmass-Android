package vn.vistark.pharmass.core.api.response


import com.google.gson.annotations.SerializedName
import vn.vistark.pharmass.core.api.response.PharmacySimpleOwner

data class SupplierSimplePharmacy(
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("created_at")
    var createdAt: String = "",
    @SerializedName("updated_at")
    var updatedAt: String = "",
    @SerializedName("name")
    var name: String = "",
    @SerializedName("address")
    var address: String = "",
    @SerializedName("phoneNumber")
    var phoneNumber: String = "",
    @SerializedName("pharmacy")
    var pharmacy: Int = -1
)