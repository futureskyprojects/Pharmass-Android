package vn.vistark.pharmass.core.model

import vn.vistark.pharmass.core.constants.vietnam.BaseVietnamUnit
import vn.vistark.pharmass.core.constants.vietnam.District

data class UserAddress(
    var address: String = "",
    var province: BaseVietnamUnit = BaseVietnamUnit(),
    var district: BaseVietnamUnit = BaseVietnamUnit(),
    var wards: BaseVietnamUnit = BaseVietnamUnit()
)