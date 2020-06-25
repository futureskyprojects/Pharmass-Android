package vn.vistark.pharmass.core.model


import androidx.databinding.BaseObservable
import com.google.gson.annotations.SerializedName
import vn.vistark.pharmass.core.api.response.GoodsCategorySimplePharmacy
import vn.vistark.pharmass.core.api.response.PharmacySimpleOwner
import vn.vistark.pharmass.core.api.response.SupplierSimplePharmacy

data class Goods(
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("name")
    var name: String = "",
    @SerializedName("abbreviation")
    var abbreviation: String = "",
    @SerializedName("images")
    var images: List<String> = emptyList(),
    @SerializedName("barcode")
    var barcode: String = "",
    @SerializedName("manufacturerCountry")
    var manufacturerCountry: String = "",
    @SerializedName("description")
    var description: String = "",
    @SerializedName("unit")
    var unit: String = "",
    @SerializedName("weight")
    var weight: Float = 0F,
    @SerializedName("packing")
    var packing: String = "",
    @SerializedName("routeUsed")
    var routeUsed: String = "",
    @SerializedName("inventoryAtleast")
    var inventoryAtleast: Float = 0F,
    @SerializedName("inventoryMost")
    var inventoryMost: Float = 9999999F,
    @SerializedName("entryPrice")
    var entryPrice: Double = 0.0,
    @SerializedName("exportPrice")
    var exportPrice: Double = 0.0,
    @SerializedName("amount")
    var amount: Float = 0F,
    @SerializedName("created_at")
    var createdAt: String = "",
    @SerializedName("updated_at")
    var updatedAt: String = "",
    @SerializedName("supplier")
    var supplier: SupplierSimplePharmacy? = null,
    @SerializedName("goods_category")
    var goodsCategory: GoodsCategorySimplePharmacy? = null,
    @SerializedName("medicine_category")
    var medicineCategory: MedicineCategory? = null,
    var isPublic: Boolean = true,
    var pharmacy: PharmacySimpleOwner = PharmacySimpleOwner()
) : BaseObservable() {
    fun validate(): String {
        if (name.isEmpty()) {
            return "Vui lòng nhập tên sản phẩm hoặc chọn tên thuốc"
        } else if (unit.isEmpty()) {
            return "Vui lòng nhập đơn vị"
        }
        return ""
    }
}