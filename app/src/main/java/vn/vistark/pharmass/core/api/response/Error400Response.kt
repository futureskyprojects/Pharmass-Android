package vn.vistark.pharmass.core.api.response


import com.google.gson.annotations.SerializedName

data class Error400Response(
    @SerializedName("statusCode")
    var statusCode: Int = 0,
    @SerializedName("error")
    var error: String = "",
    @SerializedName("message")
    var message: List<Message> = listOf(),
    @SerializedName("data")
    var `data`: List<Data> = listOf()
)