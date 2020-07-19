package vn.vistark.pharmass.ui.pharmacy.fragments.statistical

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.charts.PieChart
import com.github.ybq.android.spinkit.SpinKitView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import vn.vistark.pharmass.R
import vn.vistark.pharmass.component.pharmacy_options_picker.PharmacyOptionsPickerActivity
import vn.vistark.pharmass.component.time_range_picker.TimeRangePickerActivity
import vn.vistark.pharmass.core.constants.RequestCode
import vn.vistark.pharmass.core.model.Bill
import vn.vistark.pharmass.core.model.Pharmacy
import vn.vistark.pharmass.event_bus.BroadCastEvent
import vn.vistark.pharmass.processing.GetBillByPharmacyIdProcessing
import vn.vistark.pharmass.ui.pharmacy.PharmacyActivity


class StatisticalFragment : Fragment(), BroadCastEvent {
    private var pharmacyJson: String? = null
    var pharmacy: Pharmacy? = null

    lateinit var pcRatioOfBillPerStaff: PieChart
    lateinit var ccRevenue: CombinedChart
    lateinit var tvStatistical: TextView
    lateinit var skvLoadingIcon: SpinKitView
    lateinit var rvTopTrendingGoods: RecyclerView

    lateinit var lnPharmacyOptionShowButton: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            pharmacyJson = it.getString(ARG_PHARMACY_KEY)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_statistical, container, false)
        if (pharmacyJson != null && pharmacyJson!!.isNotEmpty()) {
            pharmacy = Gson().fromJson(pharmacyJson, Pharmacy::class.java)
        }
        if (pharmacy != null) {
            (activity as PharmacyActivity).initEventBus(this)
            // OK
            initViews(v)
            initEvents()
            loadingData()
        } else {
            // Exit
            Toast.makeText(
                context,
                "Không xác định được nhà thuốc hiện đang thao tác để có thể lấy số liệu thống kê. Vui lòng thử vào lại!",
                Toast.LENGTH_SHORT
            ).show()
            (context as Activity).finish()
        }
        return v
    }

    private fun initEvents() {
        tvStatistical.setOnClickListener {
            val intent = Intent(context, TimeRangePickerActivity::class.java)
            intent.putExtra(
                Pharmacy::class.java.simpleName,
                pharmacyJson
            )
            activity?.startActivityForResult(intent, RequestCode.REQUEST_TIME_RANGE_PICKER_CODE)
            activity?.overridePendingTransition(0, 300)
        }

        lnPharmacyOptionShowButton.setOnClickListener {
            val intent = Intent(context, PharmacyOptionsPickerActivity::class.java)
            activity?.startActivityForResult(
                intent,
                RequestCode.REQUEST_PHARMACY_OPTION_PICKER_CODE
            )
            activity?.overridePendingTransition(0, 300)
        }
    }

    private fun loadingData() {
        GetBillByPharmacyIdProcessing(context!!, pharmacy!!.id)
            .onFinished = { bills ->
            if (bills != null && bills.isNotEmpty()) {
                initCharts(bills)
            }
        }
    }

    private fun initCharts(bills: List<Bill>) {
        skvLoadingIcon.visibility = View.VISIBLE
        rvTopTrendingGoods.visibility = View.GONE
        bills.sortedByDescending { it.createdAt }
        RatioOfBillPerStaff(pcRatioOfBillPerStaff, bills)
        Revenue(ccRevenue, bills)
        TopTrendingGoods(rvTopTrendingGoods, skvLoadingIcon, bills)
    }

    private fun initViews(v: View) {
        pcRatioOfBillPerStaff = v.findViewById(R.id.pcRatioOfBillPerStaff)
        ccRevenue = v.findViewById(R.id.ccRevenue)
        tvStatistical = v.findViewById(R.id.tvStatistical)
        skvLoadingIcon = v.findViewById(R.id.skvLoadingIcon)
        rvTopTrendingGoods = v.findViewById(R.id.rvTopTrendingGoods)
        lnPharmacyOptionShowButton = v.findViewById(R.id.lnPharmacyOptionShowButton)
    }

    companion object {
        const val ARG_PHARMACY_KEY = "ARG_PHARMACY_KEY"

        @JvmStatic
        fun newInstance(pharmacy: String) =
            StatisticalFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PHARMACY_KEY, pharmacy)
                }
            }
    }


    override fun receivedString(key: String, value: String) {
        if (key == Bill::class.java.simpleName) {
            val bills = Gson().fromJson<List<Bill>>(value, object : TypeToken<List<Bill>>() {}.type)
            initCharts(bills)
        } else if (key == TimeRangePickerActivity::class.java.simpleName) {
            tvStatistical.text = value
        }
    }

}