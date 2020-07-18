package vn.vistark.pharmass.ui.pharmacy.pharmacy_detail

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import vn.vistark.pharmass.core.model.Pharmacy

class PharmacyDetailPagerAdapter(fm: FragmentManager, val pharmacy: Pharmacy) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return PharmacyDetailBottomMenu.all(
            pharmacy
        )[position].fragment!!
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return PharmacyDetailBottomMenu.all(
            pharmacy
        )[position].title
    }

    override fun getCount(): Int = PharmacyDetailBottomMenu.all(
        pharmacy
    ).size
}