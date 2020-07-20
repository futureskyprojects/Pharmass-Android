package vn.vistark.pharmass.component.view_all_bill

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_view_all_bill.*
import kotlinx.android.synthetic.main.notify_no_data.*
import vn.vistark.pharmass.R
import vn.vistark.pharmass.core.model.Bill
import vn.vistark.pharmass.processing.GetBillByPharmacyIdAndPatientIdProcessing
import vn.vistark.pharmass.processing.GetBillByPharmacyIdAndStaffUserIdProcessing
import vn.vistark.pharmass.ui.pharmacy.fragments.bill.BillAdapter

class ViewAllBillActivity : AppCompatActivity() {

    companion object {
        fun start(
            context: Context,
            pharmacyId: Int,
            userId: Int,
            isBillsOfPatient: Boolean = true
        ) {
            val intent = Intent(context, ViewAllBillActivity::class.java)
            val baseBillOfInfo = BaseBillOfInfo(pharmacyId, userId, isBillsOfPatient)
            intent.putExtra(
                BaseBillOfInfo::class.java.simpleName,
                Gson().toJson(baseBillOfInfo)
            )
            (context as AppCompatActivity).overridePendingTransition(0, 300)
            context.startActivity(intent)
        }

        data class BaseBillOfInfo(
            val pharmacyId: Int,
            val userId: Int,
            val isBillsOfPatient: Boolean = false
        )
    }

    var baseBillOfInfo: BaseBillOfInfo? = null

    var bills = ArrayList<Bill>()
    lateinit var adapter: BillAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_all_bill)
        setResult(Activity.RESULT_CANCELED)

        if (getData()) {
            showLoad()
            inits()
            initEvents()
            initBaseView()
            loadData()
        } else {
            Toast.makeText(
                this,
                "Không thể xác dịnh thông tin của người này, vui lòng thử lại!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun loadData() {
        if (baseBillOfInfo!!.isBillsOfPatient) {
            GetBillByPharmacyIdAndPatientIdProcessing(
                this,
                baseBillOfInfo!!.pharmacyId,
                baseBillOfInfo!!.userId,
                true
            ).onFinished = {
                processing(it)
            }
        } else {
            GetBillByPharmacyIdAndStaffUserIdProcessing(
                this,
                baseBillOfInfo!!.pharmacyId,
                baseBillOfInfo!!.userId,
                true
            ).onFinished = {
                processing(it)
            }
        }
    }

    private fun processing(bills: List<Bill>?) {
        showDetail()
        if (bills == null) {
            Toast.makeText(
                this,
                "Không thể lấy danh sách đơn của người dùng này. Vui lòng thử lại!",
                Toast.LENGTH_SHORT
            ).show()
        } else if (bills.isEmpty()) {
            tvNoData.visibility = View.VISIBLE
        } else {
            this.bills.clear()
            this.bills.addAll(bills)
            adapter.notifyDataSetChanged()
        }
    }

    private fun initBaseView() {
        rvInfoDisplayArea.setHasFixedSize(true)
        rvInfoDisplayArea.layoutManager = LinearLayoutManager(this)

        adapter = BillAdapter(bills)
        rvInfoDisplayArea.adapter = adapter

        tvTheHint.text =
            if (baseBillOfInfo!!.isBillsOfPatient) "Danh sách đơn mua" else "Danh sách đơn bán"
    }

    private fun getData(): Boolean {
        val baseBillOfInfoJson =
            intent.getStringExtra(BaseBillOfInfo::class.java.simpleName) ?: ""
        baseBillOfInfo = Gson().fromJson(baseBillOfInfoJson, BaseBillOfInfo::class.java)
        return (baseBillOfInfoJson.isNotEmpty() && baseBillOfInfo != null)
    }

    private fun inits() {
        val slideUpAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_up)
        lnMenuContainer.startAnimation(slideUpAnimation)
    }

    private fun initEvents() {
        rlHomeMenuRoot.setOnClickListener {
            rlHomeMenuRoot.setBackgroundColor(Color.TRANSPARENT)
            onBackPressed()
        }

        ivCloseButton.setOnClickListener {
            rlHomeMenuRoot.setBackgroundColor(Color.TRANSPARENT)
            onBackPressed()
        }
    }

    fun showLoad() {
        rvInfoDisplayArea.visibility = View.GONE
        skvLoadingIcon.visibility = View.VISIBLE
    }

    fun showDetail() {
        rvInfoDisplayArea.visibility = View.VISIBLE
        skvLoadingIcon.visibility = View.GONE
    }
}