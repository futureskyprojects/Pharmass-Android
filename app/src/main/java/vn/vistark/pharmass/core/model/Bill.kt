package vn.vistark.pharmass.core.model


import com.google.gson.annotations.SerializedName

data class Bill(
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("note")
    var note: String = "",
    @SerializedName("images")
    var images: List<String> = listOf(),
    @SerializedName("pharmacy_staff")
    var pharmacyStaff: SimplePharmacyStaff = SimplePharmacyStaff(),
    @SerializedName("patient")
    var patient: User? = null,
    @SerializedName("conclude")
    var conclude: String = "",
    @SerializedName("created_at")
    var createdAt: String = "",
    @SerializedName("updated_at")
    var updatedAt: String = "",
    @SerializedName("bill_items")
    var simpleBillItems: List<SimpleBillItem> = listOf()
)