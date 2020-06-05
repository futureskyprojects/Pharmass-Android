package vn.vistark.pharmass.ui.pharmacy

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class PharmacyPagerAdapter(fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return PharmacyBottomMenu.all[position].fragment!!
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return PharmacyBottomMenu.all[position].title
    }

    override fun getCount(): Int = PharmacyBottomMenu.all.size
}