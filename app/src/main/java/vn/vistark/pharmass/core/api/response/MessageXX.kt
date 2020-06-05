package vn.vistark.pharmass.core.api.response


import com.google.gson.annotations.SerializedName

data class MessageXX(
    @SerializedName("id")
    var id: String = "",
    @SerializedName("message")
    var message: String = ""
)