package vn.vistark.pharmass.ui.pharmacy

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import vn.vistark.pharmass.core.model.Pharmacy

class PharmacyPagerAdapter(fm: FragmentManager, val pharmacy: Pharmacy?) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return PharmacyBottomMenu.all(pharmacy)[position].fragment!!
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return PharmacyBottomMenu.all(pharmacy)[position].title
    }

    override fun getCount(): Int = PharmacyBottomMenu.all(pharmacy).size
}