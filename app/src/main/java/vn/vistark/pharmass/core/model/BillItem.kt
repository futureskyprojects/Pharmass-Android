package vn.vistark.pharmass.core.model


import com.google.gson.annotations.SerializedName

data class BillItem(
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("dosage")
    var dosage: Int = 0,
    @SerializedName("direction")
    var direction: String = "",
    @SerializedName("created_at")
    var createdAt: String = "",
    @SerializedName("updated_at")
    var updatedAt: String = "",
    @SerializedName("goods")
    var goods: Int = 0,
    var tempGoods: Goods? = Goods()
)