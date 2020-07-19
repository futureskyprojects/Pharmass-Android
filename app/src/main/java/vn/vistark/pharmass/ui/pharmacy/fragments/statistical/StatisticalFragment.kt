package vn.vistark.pharmass.ui.pharmacy.fragments.statistical

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.github.mikephil.charting.charts.PieChart
import com.google.gson.Gson
import vn.vistark.pharmass.R
import vn.vistark.pharmass.core.model.Pharmacy
import vn.vistark.pharmass.ui.pharmacy.fragments.staff.StaffFragment
import vn.vistark.pharmass.utils.DialogNotify

class StatisticalFragment : Fragment() {
    private var pharmacyJson: String? = null
    var pharmacy: Pharmacy? = null

    lateinit var pcRatioOfBillPerStaff: PieChart

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
            // OK
            initViews(v)
            initCharts()
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

    private fun initCharts() {
        RatioOfBillPerStaff(pcRatioOfBillPerStaff, pharmacy!!)
    }

    private fun initViews(v: View) {
        pcRatioOfBillPerStaff = v.findViewById(R.id.pcRatioOfBillPerStaff)
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
}