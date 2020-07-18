package vn.vistark.pharmass.ui.pharmacy.fragments.customer

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
import vn.vistark.pharmass.component.patient_picker.PatientAdapter
import vn.vistark.pharmass.core.model.Pharmacy
import vn.vistark.pharmass.core.model.User
import vn.vistark.pharmass.processing.GetBillByPharmacyIdProcessing
import vn.vistark.pharmass.ui.pharmacy.fragments.bill.BillFragment
import vn.vistark.pharmass.utils.DialogNotify

class CustomerFragment : Fragment() {
    private var pharmacyJson: String? = null
    var pharmacy: Pharmacy? = null

    var patients = ArrayList<User>()
    lateinit var adapter: PatientAdapter

    lateinit var loadingIcon: SpinKitView
    lateinit var rvCustomer: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            pharmacyJson = it.getString(BillFragment.ARG_PHARMACY_KEY)
            println("Đã nhận: $pharmacyJson")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_customer, container, false)
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
            DialogNotify.error(
                context!!,
                "Không xác định được nhà thuốc hiện đang thao tác để có thể lấy danh sách đơn bán. Vui lòng thử vào lại!"
            )
        } else {
            GetBillByPharmacyIdProcessing(context!!, pharmacy!!.id).onFinished = {
                if (it != null) {
                    loadingIcon.visibility = View.GONE
                    patients.clear()
                    it.forEach {
                        if (it.patient != null) {
                            if (patients.filter { p -> p.id != it.patient!!.id }.isEmpty()) {
                                patients.add(it.patient!!)
                                adapter.notifyDataSetChanged()
                            }
                        }
                    }
                } else {
                    // Tiến hành lấy lại danh sách đơn bán
                    loadBills()
                }
            }
        }
    }

    private fun initRecyclerView() {
        rvCustomer.setHasFixedSize(true)
        rvCustomer.layoutManager = LinearLayoutManager(context)

        adapter = PatientAdapter(patients)
        rvCustomer.adapter = adapter
        adapter.onClicked = {
            Toast.makeText(context, "Xem chi tiet", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initViews(v: View) {
        loadingIcon = v.findViewById(R.id.loadingIcon)
        rvCustomer = v.findViewById(R.id.rvCustomer)
    }

    companion object {
        const val ARG_PHARMACY_KEY = "ARG_PHARMACY_KEY"

        @JvmStatic
        fun newInstance() =
            CustomerFragment()

        @JvmStatic
        fun newInstance(pharmacy: String) =
            CustomerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PHARMACY_KEY, pharmacy)
                }
            }
    }
}