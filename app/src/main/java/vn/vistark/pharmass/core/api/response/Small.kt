package vn.vistark.pharmass.core.api.response


import com.google.gson.annotations.SerializedName

data class Small(
    @SerializedName("ext")
    var ext: String = "",
    @SerializedName("url")
    var url: String = "",
    @SerializedName("hash")
    var hash: String = "",
    @SerializedName("mime")
    var mime: String = "",
    @SerializedName("path")
    var path: Any = Any(),
    @SerializedName("size")
    var size: Double = 0.0,
    @SerializedName("width")
    var width: Int = 0,
    @SerializedName("height")
    var height: Int = 0
)