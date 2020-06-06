package vn.vistark.pharmass.core.api.response


import com.google.gson.annotations.SerializedName

data class Formats(
    @SerializedName("small")
    var small: Small = Small(),
    @SerializedName("thumbnail")
    var thumbnail: Thumbnail = Thumbnail()
)