package vn.vistark.pharmass.core.constants.vietnam


import com.google.gson.annotations.SerializedName

data class District(
    @SerializedName("name")
    var name: String = "",
    @SerializedName("type")
    var type: String = "",
    @SerializedName("slug")
    var slug: String = "",
    @SerializedName("name_with_type")
    var nameWithType: String = "",
    @SerializedName("path")
    var path: String = "",
    @SerializedName("path_with_type")
    var pathWithType: String = "",
    @SerializedName("code")
    var code: String = "",
    @SerializedName("parent_code")
    var parentCode: String = "",
    @SerializedName("xa-phuong")
    var xaPhuong: List<Wards> = listOf()
)