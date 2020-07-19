package vn.vistark.pharmass.core.model


import com.google.gson.annotations.SerializedName

data class SimplePharmacyStaff(
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("positionCode")
    var positionCode: String = "",
    @SerializedName("user")
    var user: Int = 0,
    @SerializedName("pharmacy")
    var pharmacy: Int = 0,
    @SerializedName("created_at")
    var createdAt: String = "",
    @SerializedName("updated_at")
    var updatedAt: String = ""
) {
    fun from(pharmacyStaff: PharmacyStaff) {
        id = pharmacyStaff.id
        positionCode = pharmacyStaff.positionCode
        user = pharmacyStaff.user.id
        pharmacy = pharmacyStaff.pharmacy.id
        createdAt = pharmacyStaff.createAt
        updatedAt = pharmacyStaff.updateAt
    }
}