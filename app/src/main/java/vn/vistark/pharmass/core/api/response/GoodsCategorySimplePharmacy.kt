package vn.vistark.pharmass.core.api.response

import com.google.gson.annotations.SerializedName
import vn.vistark.pharmass.core.api.response.PharmacySimpleOwner
import vn.vistark.pharmass.core.model.GoodsCategory

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
    companion object {
        fun fromGoodsCategory(goodsCategory: GoodsCategory): GoodsCategorySimplePharmacy {
            return GoodsCategorySimplePharmacy(
                goodsCategory.id,
                goodsCategory.name,
                goodsCategory.description,
                goodsCategory.pharmacy.id,
                goodsCategory.createdAt,
                goodsCategory.updatedAt
            )
        }
    }
}