package vn.vistark.pharmass.component.time_range_picker

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_time_range_picker.*
import vn.vistark.pharmass.R
import vn.vistark.pharmass.component.address_picker.AddressPickerActivity
import vn.vistark.pharmass.core.model.Bill
import vn.vistark.pharmass.core.model.Pharmacy
import vn.vistark.pharmass.processing.GetBillByPharmacyIdAndTimeRangeProcessing
import vn.vistark.pharmass.processing.GetBillByPharmacyIdProcessing
import vn.vistark.pharmass.ui.pharmacy.fragments.statistical.StatisticalFragment
import vn.vistark.pharmass.utils.DateTimeUtils
import java.util.*

class TimeRangePickerActivity : AppCompatActivity() {
    var pharmacy: Pharmacy? = null

    var fromDate = ""
    var toDate = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_range_picker)
        setResult(Activity.RESULT_CANCELED)
        if (readData()) {
            inits()
            initEvents()
        } else {
            Toast.makeText(
                this,
                "Không thể lấy thông tin của nhà thuốc hiện đang thao tác, vui lòng thử lại!",
                Toast.LENGTH_SHORT
            ).show()
            finish()
        }
    }

    private fun readData(): Boolean {
        val pharmacyJson =
            intent.getStringExtra(Pharmacy::class.java.simpleName) ?: ""
        pharmacy = Gson().fromJson(pharmacyJson, Pharmacy::class.java)

        return (pharmacyJson.isNotEmpty() && pharmacy != null)
    }

    private fun initEvents() {
        rlHomeMenuRoot.setOnClickListener {
            rlHomeMenuRoot.setBackgroundColor(Color.TRANSPARENT)
            onBackPressed()
        }

        btnThisMonth.setOnClickListener {
            // Thang nay
            val cal = Calendar.getInstance()
            fromDate = String.format(
                "%4d-%02d-%02d", cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH) + 1,
                cal.getActualMinimum(Calendar.DAY_OF_MONTH)
            )
            toDate = String.format(
                "%4d-%02d-%02d", cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH) + 1,
                cal.getActualMaximum(Calendar.DAY_OF_MONTH)
            )

            processing("Thống kê tháng này")
        }

        btnNewYearFromNow.setOnClickListener {
            // dau nam den gio
            val cal = Calendar.getInstance()
            fromDate = String.format(
                "%4d-%02d-%02d", cal.get(Calendar.YEAR),
                cal.getActualMinimum(Calendar.MONTH) + 1,
                cal.getActualMinimum(Calendar.DAY_OF_MONTH)
            )
            toDate = String.format(
                "%4d-%02d-%02d", cal.get(Calendar.YEAR),
                cal.getActualMaximum(Calendar.MONTH) + 1,
                cal.getActualMaximum(Calendar.DAY_OF_MONTH)
            )

            processing("Thống kê đầu năm đến giờ")
        }

        btnCustomTimeRange.setOnClickListener {
            // Tuy Chon
            pickDate()
        }
    }

    private fun processing(pickedType: String) {
        showLoading() // Hien thi loading
        println("Đã chọn từ ngày ${fromDate} đến ngày ${toDate}")
        GetBillByPharmacyIdAndTimeRangeProcessing(this, pharmacy!!.id, fromDate, toDate)
            .onFinished = { bills ->
            val dataIntent = Intent()
            dataIntent.putExtra(
                Bill::class.java.simpleName,
                Gson().toJson(bills)
            )
            dataIntent.putExtra(
                TimeRangePickerActivity::class.java.simpleName,
                pickedType
            )
            setResult(Activity.RESULT_OK, dataIntent)
            finish()
        }
    }

    private fun pickDate(isStartDate: Boolean = true) {
        val c = Calendar.getInstance()
        DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                if (isStartDate) {
                    fromDate = String.format(
                        "%4d-%02d-%02d", year,
                        monthOfYear + 1,
                        dayOfMonth
                    )
                    pickDate(false)
                } else {
                    toDate = String.format(
                        "%4d-%02d-%02d", year,
                        monthOfYear + 1,
                        dayOfMonth
                    )
                    if (DateTimeUtils.StringToDate(
                            toDate,
                            "yyyy-MM-dd"
                        )!!.before(DateTimeUtils.StringToDate(fromDate, "yyyy-MM-dd")!!)
                    ) {
                        Toast.makeText(
                            btnCustomTimeRange.context,
                            "Ngày kết thúc phải lớn hơn ngày khởi đầu, vui lòng chọn lại!",
                            Toast.LENGTH_SHORT
                        ).show()
                        // Chon lai
                        pickDate(false)
                    } else {
                        processing("Thống kê từ ${fromDate} đến ${toDate}")
                    }
                }
            },
            c.get(Calendar.YEAR),
            c.get(Calendar.MONTH),
            c.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun inits() {
        showOptions()
        val slideUpAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_up)
        lnMenuContainer.startAnimation(slideUpAnimation)
    }

    private fun showOptions() {
        skvLoadingIcon.visibility = View.GONE
        lnMainOptionsLayout.visibility = View.VISIBLE
    }

    private fun showLoading() {
        skvLoadingIcon.visibility = View.VISIBLE
        lnMainOptionsLayout.visibility = View.GONE
    }
}