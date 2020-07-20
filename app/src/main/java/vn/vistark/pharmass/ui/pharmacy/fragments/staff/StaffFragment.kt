package vn.vistark.pharmass.ui.pharmacy.fragments.staff

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.github.ybq.android.spinkit.SpinKitView
import com.google.gson.Gson
import vn.vistark.pharmass.R
import vn.vistark.pharmass.core.model.Pharmacy
import vn.vistark.pharmass.core.model.PharmacyStaff
import vn.vistark.pharmass.processing.GetPharmacyStaffByPharmacyProcessing

class StaffFragment : Fragment() {
    private var pharmacyJson: String? = null
    var pharmacy: Pharmacy? = null

    lateinit var rvStaffs: RecyclerView
    lateinit var loadingIcon: SpinKitView

    val staffs = ArrayList<PharmacyStaff>()
    lateinit var adapter: PharmacyStaffAdapter

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
        val v = inflater.inflate(R.layout.fragment_staff, container, false)

        rvStaffs = v.findViewById(R.id.rvStaffs)
        loadingIcon = v.findViewById(R.id.loadingIcon)

        loadStaffs()

        return v
    }

    private fun loadStaffs() {
        if (pharmacyJson != null && pharmacyJson!!.isNotEmpty()) {
            pharmacy = Gson().fromJson(pharmacyJson, Pharmacy::class.java)
        }
        rvStaffs.layoutManager = LinearLayoutManager(context)
        rvStaffs.setHasFixedSize(true)

        adapter = PharmacyStaffAdapter(staffs)
        rvStaffs.adapter = adapter

        if (pharmacy == null) {
            SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE).apply {
                titleText = "Không thể truy cập vào thông tin của nhà thuốc này"
                contentText = "THÔNG TIN TRỐNG"

                setCancelable(false)
                showCancelButton(false)
                setConfirmButton("QUAY LẠI") {
                    it.dismiss()
                    activity?.finish()
                }
                show()
            }
            return
        } else {
            GetPharmacyStaffByPharmacyProcessing(context!!, pharmacy!!.id).onFinished = {
                loadingIcon.visibility = View.GONE
                if (it != null) {
                    staffs.clear()
                    staffs.addAll(it)
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(
                        context,
                        "Hiện tại bạn chưa có nhân viên nào!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    companion object {
        const val ARG_PHARMACY_KEY = "param1"

        @JvmStatic
        fun newInstance(pharmacy: String) =
            StaffFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PHARMACY_KEY, pharmacy)
                }
            }
    }
}