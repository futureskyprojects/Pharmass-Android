package vn.vistark.pharmass.ui.pharmacy

import androidx.fragment.app.Fragment
import com.google.gson.Gson
import vn.vistark.pharmass.core.model.Pharmacy
import vn.vistark.pharmass.ui.pharmacy.fragments.bill.BillFragment
import vn.vistark.pharmass.ui.pharmacy.fragments.category.CategoryFragment
import vn.vistark.pharmass.ui.pharmacy.fragments.customer.CustomerFragment
import vn.vistark.pharmass.ui.pharmacy.fragments.inventory_limit.InventoryLimitFragment
import vn.vistark.pharmass.ui.pharmacy.fragments.staff.StaffFragment
import vn.vistark.pharmass.ui.pharmacy.fragments.statistical.StatisticalFragment
import vn.vistark.pharmass.ui.pharmacy.fragments.supplier.SupplierFragment

data class PharmacyBottomMenu(var title: String, var fragment: Fragment?) {
    companion object {
        fun all(pharmacy: Pharmacy?): Array<PharmacyBottomMenu> {
            val pharmacyJson = Gson().toJson(pharmacy)
            return arrayOf(
                PharmacyBottomMenu("Nhân viên", StaffFragment.newInstance(pharmacyJson)),
                PharmacyBottomMenu("Mục sản phẩm", CategoryFragment.newInstance(pharmacyJson)),
                PharmacyBottomMenu("Giới hạn kho", InventoryLimitFragment.newInstance(pharmacyJson)),
                PharmacyBottomMenu("Nhà cung cấp", SupplierFragment.newInstance(pharmacyJson)),
                PharmacyBottomMenu("Khách hàng", CustomerFragment.newInstance(pharmacyJson)),
                PharmacyBottomMenu("Đơn bán", BillFragment.newInstance(pharmacyJson)),
                PharmacyBottomMenu("Thống kê", StatisticalFragment.newInstance(pharmacyJson))
            )
        }
    }
}