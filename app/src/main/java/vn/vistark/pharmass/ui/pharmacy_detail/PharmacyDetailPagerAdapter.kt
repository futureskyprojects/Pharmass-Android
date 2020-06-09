package vn.vistark.pharmass.ui.pharmacy_detail

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class PharmacyDetailPagerAdapter(fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return PharmacyDetailBottomMenu.all[position].fragment!!
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return PharmacyDetailBottomMenu.all[position].title
    }

    override fun getCount(): Int = PharmacyDetailBottomMenu.all.size
}