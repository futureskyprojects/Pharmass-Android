package vn.vistark.pharmass.ui.pharmacy_detail

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_pharmacy.*
import kotlinx.android.synthetic.main.activity_pharmacy_detail.*
import kotlinx.android.synthetic.main.activity_pharmacy_detail.tabLayout
import kotlinx.android.synthetic.main.activity_pharmacy_detail.viewPager
import vn.vistark.pharmass.R
import vn.vistark.pharmass.core.model.Pharmacy
import vn.vistark.pharmass.ui.pharmacy.PharmacyBottomMenu
import vn.vistark.pharmass.ui.pharmacy.PharmacyPagerAdapter
import vn.vistark.pharmass.utils.GlideUtils
import java.lang.Exception

class PharmacyDetailActivity : AppCompatActivity() {
    lateinit var pharmacy: Pharmacy
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pharmacy_detail)
        val pharmacyJson =
            intent.getStringExtra(PharmacyDetailActivity::class.java.simpleName) ?: ""
        println("$pharmacyJson >>>>>>>>>>>>>>>>>>>>>")

        initEvents()
        inits()
        initBottomMenu()
    }

    private fun initBottomMenu() {
        // Thêm các tab vào cho menu
        PharmacyDetailBottomMenu.all(pharmacy).forEach {
            tabLayout.addTab(tabLayout.newTab().setText(it.title))
        }
        viewPager.adapter = PharmacyDetailPagerAdapter(supportFragmentManager, pharmacy)
        tabLayout.setupWithViewPager(viewPager)
    }

    @SuppressLint("SetTextI18n")
    private fun showDetails() {
        GlideUtils.loadToImageViewWithPlaceHolder(
            ivUserPharmacyFeatureImages,
            pharmacy.getFeatureImageFullAddress(),
            R.drawable.pharmacy_background
        )
        GlideUtils.loadToImageViewWithPlaceHolder(
            ivUserPharmacyLogo,
            pharmacy.getLogoImageFullAddress(),
            R.drawable.no_logo
        )
        tvUserPharmacyName.text = pharmacy.name
        tvUserPharmacyName.isSelected = true

//        tvuserPharmacyRatingNumber

        tvUserPharmacyWorkTime.text = "${pharmacy.openTime} - ${pharmacy.closeTime}"
        tvUserPharmacyWorkTime.isSelected = true

        tvUserPharmacyAdress.text = pharmacy.address.toString()
        tvUserPharmacyAdress.isSelected = true

        tvUserPharmacyShortDescription.text = pharmacy.shortDescription
        tvUserPharmacyShortDescription.isSelected = true
    }

    private fun initEvents() {
        rlHomeMenuRoot.setOnClickListener {
            rlHomeMenuRoot.setBackgroundColor(Color.TRANSPARENT)
            onBackPressed()
        }
    }

    private fun inits() {
        val slideUpAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_up)
        lnMenuContainer.startAnimation(slideUpAnimation)

        val pharmacyJson =
            intent.getStringExtra(PharmacyDetailActivity::class.java.simpleName) ?: ""
        println(pharmacyJson)
        if (pharmacyJson.isEmpty()) {
            stopShowDetail()
            return
        }
        try {
            pharmacy = Gson().fromJson(pharmacyJson, Pharmacy::class.java)
        } catch (e: Exception) {
            stopShowDetail()
            return
        }
        showDetails()
    }

    fun stopShowDetail() {
        Toast.makeText(this, "Không thể xem thông tin của nhà thuốc này", Toast.LENGTH_SHORT).show()
        rlHomeMenuRoot.performClick()
    }
}