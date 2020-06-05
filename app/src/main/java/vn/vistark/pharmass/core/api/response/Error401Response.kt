package vn.vistark.pharmass.core.api.response


import com.google.gson.annotations.SerializedName

data class Error401Response(
    @SerializedName("statusCode")
    var statusCode: Int = 0,
    @SerializedName("error")
    var error: String = "",
    @SerializedName("message")
    var message: String = ""
)