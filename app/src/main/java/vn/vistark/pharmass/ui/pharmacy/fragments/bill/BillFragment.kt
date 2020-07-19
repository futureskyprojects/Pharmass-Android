package vn.vistark.pharmass.ui.pharmacy.fragments.bill

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.ybq.android.spinkit.SpinKitView
import com.google.gson.Gson
import vn.vistark.pharmass.R
import vn.vistark.pharmass.core.model.Bill
import vn.vistark.pharmass.core.model.Pharmacy
import vn.vistark.pharmass.processing.GetBillByPharmacyIdProcessing
import vn.vistark.pharmass.utils.DialogNotify

class BillFragment : Fragment() {
    private var pharmacyJson: String? = null
    var pharmacy: Pharmacy? = null

    var bills = ArrayList<Bill>()
    lateinit var adapter: BillAdapter

    lateinit var loadingIcon: SpinKitView
    lateinit var rvBills: RecyclerView


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
        val v = inflater.inflate(R.layout.fragment_bill, container, false)
        initViews(v)
        initRecyclerView()
        loadBills()

        return v
    }

    private fun loadBills() {
        if (pharmacyJson != null && pharmacyJson!!.isNotEmpty()) {
            pharmacy = Gson().fromJson(pharmacyJson, Pharmacy::class.java)
        }
        if (pharmacy == null) {
            Toast.makeText(
                context,
                "Không xác định được nhà thuốc hiện đang thao tác để có thể lấy danh sách đơn bán. Vui lòng thử vào lại!",
                Toast.LENGTH_SHORT
            ).show()
            (context as Activity).finish()
        } else {
            GetBillByPharmacyIdProcessing(context!!, pharmacy!!.id).onFinished = {
                if (it != null) {
                    loadingIcon.visibility = View.GONE
                    bills.clear()
                    bills.addAll(it.sortedByDescending { it.id })
                    adapter.notifyDataSetChanged()
                } else {
                    // Tiến hành lấy lại danh sách đơn bán
                    loadBills()
                }
            }
        }
    }

    private fun initRecyclerView() {
        rvBills.setHasFixedSize(true)
        rvBills.layoutManager = LinearLayoutManager(context)

        adapter = BillAdapter(bills)
        rvBills.adapter = adapter
        adapter.onClicked = {
            Toast.makeText(context, "Xem chi tiet", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initViews(v: View) {
        loadingIcon = v.findViewById(R.id.loadingIcon)
        rvBills = v.findViewById(R.id.rvBills)
    }

    companion object {
        const val ARG_PHARMACY_KEY = "ARG_PHARMACY_KEY"

        @JvmStatic
        fun newInstance(pharmacy: String) =
            BillFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PHARMACY_KEY, pharmacy)
                }
            }
    }
}