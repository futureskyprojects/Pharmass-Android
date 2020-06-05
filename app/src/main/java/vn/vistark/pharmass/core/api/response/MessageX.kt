package vn.vistark.pharmass.core.api.response


import com.google.gson.annotations.SerializedName

data class MessageX(
    @SerializedName("id")
    var id: String = "",
    @SerializedName("message")
    var message: String = ""
)