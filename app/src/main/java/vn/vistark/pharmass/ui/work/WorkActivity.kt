package vn.vistark.pharmass.ui.work

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import kotlinx.android.synthetic.main.component_work_top_pharmacies.*
import kotlinx.android.synthetic.main.components_search_bar.*
import kotlinx.android.synthetic.main.components_toolbar.*
import vn.vistark.pharmass.R
import vn.vistark.pharmass.core.model.Pharmacy
import vn.vistark.pharmass.processing.GetUserPharmaciesProcessing
import vn.vistark.pharmass.ui.pharmacy_updater.PharmacyUpdaterActivity

class WorkActivity : AppCompatActivity() {
    val userPharmacies: ArrayList<Pharmacy> = ArrayList()
    lateinit var userPharmaciesAdapter: CurrentUserPharmacyAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work)
        inits()
        initEvents()
        initListPharmacy()
        window.decorView.clearFocus()
    }

    private fun initListPharmacy() {
        userPharmaciesAdapter = CurrentUserPharmacyAdapter(userPharmacies)

        rvCurrentUserPharmacies.setHasFixedSize(true)
        rvCurrentUserPharmacies.layoutManager = LinearLayoutManager(this)
        rvCurrentUserPharmacies.adapter = userPharmaciesAdapter
        GetUserPharmaciesProcessing(this).onFinished = {
            loadingPharmaciesIcon.visibility = View.GONE
            if (it != null) {
                it.forEach {
                    userPharmacies.add(it)
                    userPharmaciesAdapter.notifyDataSetChanged()
                }
            } else {

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