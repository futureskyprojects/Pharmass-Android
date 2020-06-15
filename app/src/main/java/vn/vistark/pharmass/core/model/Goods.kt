package vn.vistark.pharmass.core.model


import com.google.gson.annotations.SerializedName
import vn.vistark.pharmass.core.api.response.GoodsCategorySimplePharmacy
import vn.vistark.pharmass.core.api.response.SupplierSimplePharmacy

data class Goods(
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("name")
    var name: String = "",
    @SerializedName("abbreviation")
    var abbreviation: String = "",
    @SerializedName("images")
    var images: Any = Any(),
    @SerializedName("barcode")
    var barcode: String = "",
    @SerializedName("manufacturerCountry")
    var manufacturerCountry: String = "",
    @SerializedName("description")
    var description: String = "",
    @SerializedName("unit")
    var unit: String = "",
    @SerializedName("weight")
    var weight: Int = 0,
    @SerializedName("packing")
    var packing: String = "",
    @SerializedName("routeUsed")
    var routeUsed: String = "",
    @SerializedName("inventoryAtleast")
    var inventoryAtleast: Int = 0,
    @SerializedName("inventoryMost")
    var inventoryMost: Int = 0,
    @SerializedName("entryPrice")
    var entryPrice: Int = 0,
    @SerializedName("exportPrice")
    var exportPrice: Int = 0,
    @SerializedName("amount")
    var amount: Int = 0,
    @SerializedName("created_at")
    var createdAt: String = "",
    @SerializedName("updated_at")
    var updatedAt: String = "",
    @SerializedName("supplier")
    var supplier: SupplierSimplePharmacy? = null,
    @SerializedName("goods_category")
    var goodsCategory: GoodsCategorySimplePharmacy = GoodsCategorySimplePharmacy(),
    @SerializedName("medicine_category")
    var medicineCategory: MedicineCategory? = null
)