package vn.vistark.pharmass.ui.pharmacy

import androidx.fragment.app.Fragment
import com.google.gson.Gson
import vn.vistark.pharmass.core.model.Pharmacy
import vn.vistark.pharmass.ui.pharmacy.fragments.*
import vn.vistark.pharmass.ui.pharmacy.fragments.category.CategoryFragment
import vn.vistark.pharmass.ui.pharmacy.fragments.staff.StaffFragment

data class PharmacyBottomMenu(var title: String, var fragment: Fragment?) {
    companion object {
        fun all(pharmacy: Pharmacy?): Array<PharmacyBottomMenu> {
            val pharmacyJson = Gson().toJson(pharmacy)
            return arrayOf(
                PharmacyBottomMenu("Nhân viên", StaffFragment.newInstance(pharmacyJson)),
                PharmacyBottomMenu("Mục sản phẩm", CategoryFragment.newInstance(pharmacyJson)),
                PharmacyBottomMenu("Khách hàng", CustomerFragment.newInstance()),
                PharmacyBottomMenu("Đơn bán", BillFragment.newInstance()),
                PharmacyBottomMenu("Thống kê", StatisticalFragment.newInstance())
            )
        }
    }
}