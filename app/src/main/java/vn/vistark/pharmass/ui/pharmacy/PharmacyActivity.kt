package vn.vistark.pharmass.ui.pharmacy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_pharmacy.*
import kotlinx.android.synthetic.main.components_toolbar.*
import vn.vistark.pharmass.R
import vn.vistark.pharmass.utils.GlideUtils

class PharmacyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pharmacy)
        inits()
        initBottomMenu()
        initEvents()
    }

    private fun initEvents() {
        ivBackButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun initBottomMenu() {
        // Thêm các tab vào cho menu
        PharmacyBottomMenu.all.forEach {
            tabLayout.addTab(tabLayout.newTab().setText(it.title))
        }
        viewPager.adapter = PharmacyPagerAdapter(supportFragmentManager)
        tabLayout.setupWithViewPager(viewPager)
    }

    private fun inits() {
        ivExpandAuthorInfomation.visibility = View.GONE
        ivBackButton.visibility = View.VISIBLE
        tvToolbarLabel.text = "Nhà thuốc Trọng Nghĩa"
        tvToolbarLabel.isSelected = true
        tvToolbarLabel.isAllCaps = false
        tvToolbarLabel.setPadding(
            tvToolbarLabel.paddingLeft,
            tvToolbarLabel.paddingTop,
            tvToolbarLabel.paddingRight,
            tvToolbarLabel.paddingBottom
        )
        civUserAvatar.setImageResource(R.drawable.ic_medical_report)
        civUserAvatar.setPadding(
            tvToolbarLabel.paddingLeft + 20,
            tvToolbarLabel.paddingTop + 20,
            tvToolbarLabel.paddingRight,
            tvToolbarLabel.paddingBottom + 20
        )
    }
}