package vn.vistark.pharmass.ui.pharmacy_detail

import androidx.fragment.app.Fragment
import vn.vistark.pharmass.ui.pharmacy.fragments.*

data class PharmacyDetailBottomMenu(var title: String, var fragment: Fragment?) {
    companion object {
        val all: Array<PharmacyDetailBottomMenu>
            get() {
                return arrayOf(
                    PharmacyDetailBottomMenu("Nhân viên", StaffFragment.newInstance()),
                    PharmacyDetailBottomMenu("Kho", WareHouseFragment.newInstance()),
                    PharmacyDetailBottomMenu("Mục sản phẩm", CategoryFragment.newInstance())
                )
            }
    }
}