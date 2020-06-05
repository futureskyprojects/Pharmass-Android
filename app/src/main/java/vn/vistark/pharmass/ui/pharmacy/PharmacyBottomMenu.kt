package vn.vistark.pharmass.ui.pharmacy

import androidx.fragment.app.Fragment
import vn.vistark.pharmass.ui.pharmacy.fragments.*

data class PharmacyBottomMenu(var title: String, var fragment: Fragment?) {
    companion object {
        val all: Array<PharmacyBottomMenu>
            get() {
                return arrayOf(
                    PharmacyBottomMenu("Nhân viên", StaffFragment.newInstance()),
                    PharmacyBottomMenu("Kho", WareHouseFragment.newInstance()),
                    PharmacyBottomMenu("Mục sản phẩm", CategoryFragment.newInstance()),
                    PharmacyBottomMenu("Khách hàng", CustomerFragment.newInstance()),
                    PharmacyBottomMenu("Đơn bán", BillFragment.newInstance()),
                    PharmacyBottomMenu("Thống kê", StatisticalFragment.newInstance())
                )
            }
    }
}