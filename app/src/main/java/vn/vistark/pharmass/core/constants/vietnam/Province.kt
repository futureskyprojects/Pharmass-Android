package vn.vistark.pharmass.core.constants.vietnam


import com.google.gson.annotations.SerializedName

data class Province(
    @SerializedName("name")
    var name: String = "",
    @SerializedName("slug")
    var slug: String = "",
    @SerializedName("type")
    var type: String = "",
    @SerializedName("name_with_type")
    var nameWithType: String = "",
    @SerializedName("code")
    var code: String = "",
    @SerializedName("quan-huyen")
    var quanHuyen: List<District> = listOf()
)