package vn.vistark.pharmass.ui.pharmacy.fragments.supplier

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.ybq.android.spinkit.SpinKitView
import com.google.gson.Gson
import vn.vistark.pharmass.R
import vn.vistark.pharmass.core.constants.RequestCode
import vn.vistark.pharmass.core.model.Pharmacy
import vn.vistark.pharmass.core.model.Supplier
import vn.vistark.pharmass.event_bus.BroadCastEvent
import vn.vistark.pharmass.processing.GetSupplierOfPharmacyProcessing
import vn.vistark.pharmass.ui.medicine_category_picker.SupplierPickerAdapter
import vn.vistark.pharmass.ui.pharmacy.PharmacyActivity
import vn.vistark.pharmass.ui.pharmacy.pharmacy_supplier_update.PharmacySupplierUpdateActivity

class SupplierFragment : Fragment(), BroadCastEvent {
    private var pharmacyJson: String? = null
    var pharmacy: Pharmacy? = null

    lateinit var rvSuppliers: RecyclerView
    lateinit var skvLoadingIcon: SpinKitView
    lateinit var lnCreateNewSupplier: LinearLayout
    lateinit var tvNoData: TextView

    val suppliers = ArrayList<Supplier>()
    lateinit var adapter: SupplierPickerAdapter

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
        val v = inflater.inflate(R.layout.fragment_supplier, container, false)
        if (pharmacyJson != null && pharmacyJson!!.isNotEmpty()) {
            pharmacy = Gson().fromJson(pharmacyJson, Pharmacy::class.java)
        }
        if (pharmacy != null) {
            (activity as PharmacyActivity).initEventBus(this)
            // OK
            initViews(v)
            initEvents()
            initRecyclerView()
            loadingData()
        } else {
            // Exit
            Toast.makeText(
                context,
                "Không xác định được nhà thuốc hiện đang thao tác. Vui lòng thử vào lại!",
                Toast.LENGTH_SHORT
            ).show()
            (context as Activity).finish()
        }
        return v
    }

    private fun initRecyclerView() {
        rvSuppliers.setHasFixedSize(true)
        rvSuppliers.layoutManager = LinearLayoutManager(context)

        adapter = SupplierPickerAdapter(suppliers)
        adapter.onClicked = { supplier ->
            val intent = Intent(context, PharmacySupplierUpdateActivity::class.java)
            intent.putExtra(
                Pharmacy::class.java.simpleName,
                pharmacyJson
            )
            intent.putExtra(
                Supplier::class.java.simpleName,
                Gson().toJson(supplier)
            )
            (context as AppCompatActivity).startActivityForResult(
                intent,
                RequestCode.REQUEST_SUPPLIER_UPDATE_CODE
            )
        }
        rvSuppliers.adapter = adapter
    }

    private fun loadingData() {
        showLoading()
        suppliers.clear()
        adapter.notifyDataSetChanged()
        GetSupplierOfPharmacyProcessing(context!!, pharmacy!!.id)
            .onFinished = { suppliers ->
            showData()
            if (suppliers != null && suppliers.isNotEmpty()) {
                this.suppliers.addAll(suppliers)
                adapter.notifyDataSetChanged()
            } else {
                tvNoData.visibility = View.VISIBLE
            }
        }
    }

    private fun initEvents() {
        lnCreateNewSupplier.setOnClickListener {
            val intent = Intent(context, PharmacySupplierUpdateActivity::class.java)
            intent.putExtra(
                Pharmacy::class.java.simpleName,
                pharmacyJson
            )
            (context as AppCompatActivity).startActivityForResult(
                intent,
                RequestCode.REQUEST_SUPPLIER_CREATE_CODE
            )
        }
    }

    private fun initViews(v: View) {
        rvSuppliers = v.findViewById(R.id.rvSuppliers)
        skvLoadingIcon = v.findViewById(R.id.skvLoadingIcon)
        lnCreateNewSupplier = v.findViewById(R.id.lnCreateNewSupplier)
        tvNoData = v.findViewById(R.id.tvNoData)
    }

    companion object {
        public const val RELOAD_SUPPLIER_LIST = "RELOAD_SUPPLIER_LIST"
        const val ARG_PHARMACY_KEY = "ARG_PHARMACY_KEY"

        @JvmStatic
        fun newInstance(pharmacy: String) =
            SupplierFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PHARMACY_KEY, pharmacy)
                }
            }
    }

    override fun receivedString(key: String, value: String) {
        if (key == RELOAD_SUPPLIER_LIST) {
            loadingData()
        }
    }

    fun showLoading() {
        skvLoadingIcon.visibility = View.VISIBLE
        rvSuppliers.visibility = View.GONE
        tvNoData.visibility = View.GONE
    }

    fun showData() {
        skvLoadingIcon.visibility = View.GONE
        rvSuppliers.visibility = View.VISIBLE
    }
}