package vn.vistark.pharmass.ui.work

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.component_work_top_pharmacies.*
import kotlinx.android.synthetic.main.components_search_bar.*
import kotlinx.android.synthetic.main.components_toolbar.*
import vn.vistark.pharmass.R
import vn.vistark.pharmass.core.constants.Constants
import vn.vistark.pharmass.core.constants.PharmacyStaffPostion
import vn.vistark.pharmass.core.model.Pharmacy
import vn.vistark.pharmass.core.model.SimplePharmacyStaff
import vn.vistark.pharmass.processing.CreateBillProcessing
import vn.vistark.pharmass.processing.CreatePharmacyStaffProcessing
import vn.vistark.pharmass.processing.GetPharmacyStaffProcessing
import vn.vistark.pharmass.processing.GetUserPharmaciesProcessing
import vn.vistark.pharmass.ui.pharmacy.pharmacy_updater.PharmacyUpdaterActivity

class WorkActivity : AppCompatActivity() {

    companion object {
        private var workActivity: WorkActivity? = null
        fun update() {
            workActivity?.initListPharmacy()
        }
    }

    val userPharmacies: ArrayList<Pharmacy> = ArrayList()
    lateinit var userPharmaciesAdapter: CurrentUserPharmacyAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work)
        inits()
        initEvents()
        initListPharmacy()
        window.decorView.clearFocus()
        workActivity = this
    }

    private fun initListPharmacy() {
        userPharmacies.clear()
        userPharmaciesAdapter = CurrentUserPharmacyAdapter(userPharmacies)

        rvCurrentUserPharmacies.setHasFixedSize(true)
        rvCurrentUserPharmacies.layoutManager = LinearLayoutManager(this)
        rvCurrentUserPharmacies.adapter = userPharmaciesAdapter
        GetUserPharmaciesProcessing(this).onFinished = {
            loadingPharmaciesIcon.visibility = View.GONE
            it?.forEach {
                userPharmacies.add(it)
                userPharmaciesAdapter.notifyDataSetChanged()
                updateOwner(it)
            }
        }
    }

    private fun updateOwner(pharmacy: Pharmacy) {
        // Cập nhật chủ sở hữu nếu chưa có
        GetPharmacyStaffProcessing(this, pharmacy.id).onFinished = { pharmacyStaffs ->
            if (pharmacyStaffs != null) {
                var isFound = false
                for (ps in pharmacyStaffs) {
                    if (ps.user.id == pharmacy.users.id && ps.positionCode ==
                        PharmacyStaffPostion.OWNER
                    ) {
                        isFound = true
                    }
                }

                // Nếu đã duyệt và chưa có chủ sở hữu cho nhà thuốc này, tiến hành cập nhật
                if (!isFound) {
                    val pharmacyStaff =
                        SimplePharmacyStaff(
                            user = Constants.user.id,
                            pharmacy = pharmacy.id,
                            positionCode = PharmacyStaffPostion.OWNER
                        )
                    CreatePharmacyStaffProcessing(this, pharmacyStaff).onFinished = {
                        if (it != null) {
                            Toast.makeText(
                                this,
                                "Đã cập nhật xong nhân viên bị thiếu cho [${pharmacy.name}]",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            updateOwner(pharmacy)
                        }
                    }
                }
            }
        }
    }

    private fun initEvents() {
        ivBackButton.setOnClickListener {
            onBackPressed()
        }
        lnCreateNewPharmacyButton.setOnClickListener {
            val intent = Intent(this, PharmacyUpdaterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun inits() {
        ivExpandAuthorInfomation.visibility = View.GONE
        ivBackButton.visibility = View.VISIBLE
        tvToolbarLabel.text = "Công việc"
        tvToolbarLabel.setPadding(
            tvToolbarLabel.paddingLeft + 25,
            tvToolbarLabel.paddingTop,
            tvToolbarLabel.paddingRight,
            tvToolbarLabel.paddingBottom
        )
        civUserAvatar.visibility = View.INVISIBLE
//        civUserAvatar.setImageResource(-1)
//        civUserAvatar.setPadding(25, 25, 25, 25)
        edtSearchContent.hint = "Tìm kiếm nhà thuốc..."
    }
}