package vn.vistark.pharmass.core.model


import com.google.gson.annotations.SerializedName

data class MedicineCategory(
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("name")
    var name: String = "",
    @SerializedName("mainActiveIngredient")
    var mainActiveIngredient: String = "",
    @SerializedName("packing")
    var packing: String = "",
    @SerializedName("unit")
    var unit: String = "",
    @SerializedName("content")
    var content: String = "",
    @SerializedName("registrationNumber")
    var registrationNumber: String = "",
    @SerializedName("manufacturerCountry")
    var manufacturerCountry: String = "",
    @SerializedName("manufacturer")
    var manufacturer: String = "",
    @SerializedName("created_at")
    var createdAt: String = "",
    @SerializedName("updated_at")
    var updatedAt: String = ""
)