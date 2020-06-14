package vn.vistark.pharmass.ui.pharmacy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_pharmacy.*
import kotlinx.android.synthetic.main.components_toolbar.*
import vn.vistark.pharmass.R
import vn.vistark.pharmass.core.model.Pharmacy
import vn.vistark.pharmass.utils.GlideUtils

class PharmacyActivity : AppCompatActivity() {
    var pharmacy: Pharmacy? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pharmacy)
        val pharmacyJson = intent.getStringExtra(PharmacyActivity::class.java.simpleName) ?: ""
        pharmacy = Gson().fromJson(pharmacyJson, Pharmacy::class.java)
        if (pharmacyJson.isEmpty() || pharmacy == null) {
            Toast.makeText(
                this,
                "Không thể tiến hành thao tác với nhà thuốc này!",
                Toast.LENGTH_SHORT
            ).show()
            finish()
            return
        }
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
        PharmacyBottomMenu.all(pharmacy).forEach {
            tabLayout.addTab(tabLayout.newTab().setText(it.title))
        }
        viewPager.adapter = PharmacyPagerAdapter(supportFragmentManager, pharmacy)
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