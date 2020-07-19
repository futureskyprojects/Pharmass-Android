package vn.vistark.pharmass.component.time_range_picker

import android.app.Activity
import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_time_range_picker.*
import vn.vistark.pharmass.R
import java.util.*

class TimeRangePickerActivity : AppCompatActivity() {

    var fromDate = ""
    var toDate = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_range_picker)
        setResult(Activity.RESULT_CANCELED)
        inits()
        initEvents()
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

            processing()
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

            processing()
        }

        btnCustomTimeRange.setOnClickListener {
            // Tuy Chon
            pickDate()
        }
    }

    private fun processing() {
        showLoading() // Hien thi loading
        println("Đã chọn từ ngày ${fromDate} đến ngày ${toDate}")
        btnThisMonth.postDelayed({
            finish()
        }, 2000)
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

                    processing()
                }
            },
            c.get(Calendar.YEAR),
            c.get(Calendar.MONTH) + 1,
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