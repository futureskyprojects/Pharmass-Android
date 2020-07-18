package vn.vistark.pharmass.ui.pharmacy.pharmacy_detail

import androidx.fragment.app.Fragment
import com.google.gson.Gson
import vn.vistark.pharmass.core.model.Pharmacy
import vn.vistark.pharmass.ui.pharmacy.fragments.*
import vn.vistark.pharmass.ui.pharmacy.pharmacy_detail.fragments.IntroductionFragment
import vn.vistark.pharmass.ui.pharmacy.pharmacy_detail.fragments.MiniMapPharmacyFragment

data class PharmacyDetailBottomMenu(var title: String, var fragment: Fragment?) {
    companion object {
        fun all(pharmacy: Pharmacy): Array<PharmacyDetailBottomMenu> {
            return arrayOf(
                PharmacyDetailBottomMenu(
                    "Giới thiệu",
                    IntroductionFragment.newInstance(
                        pharmacy.introduction
                    )
                ),
                PharmacyDetailBottomMenu(
                    "Bản đồ",
                    MiniMapPharmacyFragment.newInstance(
                        Gson().toJson(pharmacy.coordinates)
                    )
                )
//                ,                PharmacyDetailBottomMenu("Đánh giá", CategoryFragment.newInstance())
            )
        }
    }
}