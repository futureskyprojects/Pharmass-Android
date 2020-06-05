package vn.vistark.pharmass.core.model


import com.google.gson.annotations.SerializedName

data class Role(
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("name")
    var name: String = "",
    @SerializedName("description")
    var description: String = "",
    @SerializedName("type")
    var type: String = ""
)