package vn.vistark.pharmass.core.model

import com.google.gson.annotations.SerializedName
import vn.vistark.pharmass.core.api.response.PharmacySimpleOwner
import vn.vistark.pharmass.core.constants.PharmacyStaffPostion

data class PharmacyStaff(
    var id: Int = -1,
    var positionCode: String = PharmacyStaffPostion.NO_POSITION,
    @SerializedName("user")
    var user: User = User(),
    @SerializedName("pharmacy")
    var pharmacy: PharmacySimpleOwner = PharmacySimpleOwner(),
    @SerializedName("created_at")
    var createAt: String = "",
    @SerializedName("updated_at")
    var updateAt: String = ""
)