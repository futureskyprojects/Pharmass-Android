package vn.vistark.pharmass.core.api.response


import com.google.gson.annotations.SerializedName

data class BodyUploadFileResponseItem(
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("name")
    var name: String = "",
    @SerializedName("alternativeText")
    var alternativeText: Any = Any(),
    @SerializedName("caption")
    var caption: Any = Any(),
    @SerializedName("width")
    var width: Int = 0,
    @SerializedName("height")
    var height: Int = 0,
    @SerializedName("formats")
    var formats: Formats = Formats(),
    @SerializedName("hash")
    var hash: String = "",
    @SerializedName("ext")
    var ext: String = "",
    @SerializedName("mime")
    var mime: String = "",
    @SerializedName("size")
    var size: Double = 0.0,
    @SerializedName("url")
    var url: String = "",
    @SerializedName("previewUrl")
    var previewUrl: Any = Any(),
    @SerializedName("provider")
    var provider: String = "",
    @SerializedName("provider_metadata")
    var providerMetadata: Any = Any(),
    @SerializedName("created_at")
    var createdAt: String = "",
    @SerializedName("updated_at")
    var updatedAt: String = "",
    @SerializedName("related")
    var related: List<Any> = listOf()
)