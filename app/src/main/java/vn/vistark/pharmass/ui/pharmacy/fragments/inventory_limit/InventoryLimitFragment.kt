package vn.vistark.pharmass.ui.pharmacy.fragments.inventory_limit

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.github.ybq.android.spinkit.SpinKitView
import com.google.gson.Gson
import vn.vistark.pharmass.R
import vn.vistark.pharmass.component.inventory_limit_options_picker.InventoryLimitOptionPickerActivity
import vn.vistark.pharmass.core.constants.RequestCode
import vn.vistark.pharmass.core.model.GoodsCategory
import vn.vistark.pharmass.core.model.Pharmacy
import vn.vistark.pharmass.event_bus.BroadCastEvent
import vn.vistark.pharmass.processing.GetPharmacyGoodsCategoryProcessing
import vn.vistark.pharmass.ui.pharmacy.PharmacyActivity
import vn.vistark.pharmass.ui.pharmacy.fragments.category.PharmacyCategoryAdapter
import vn.vistark.pharmass.ui.pharmacy.pharmacy_ware_house.PharmacyWareHouseActivity

class InventoryLimitFragment : Fragment() {

    private var pharmacyJson: String? = null
    var pharmacy: Pharmacy? = null

    lateinit var loadingIcon: SpinKitView
    lateinit var rvCategories: RecyclerView
    lateinit var tvLimitFilterName: TextView
    lateinit var lnOpenFilterOptionsPicker: LinearLayout

    val goodsCategories = ArrayList<GoodsCategory>()
    lateinit var adapter: InventoryLimitCategoryAdapter

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
        val v = inflater.inflate(R.layout.fragment_inventory_limit, container, false)
        initViews(v)
        initEvent()
        loadCategory()
        inventoryLimitFragment = this
        return v
    }

    private fun initEvent() {
        lnOpenFilterOptionsPicker.setOnClickListener {
            val intent = Intent(context, InventoryLimitOptionPickerActivity::class.java)
            activity?.overridePendingTransition(0, 300)
            startActivity(intent)
        }
    }

    private fun loadCategory() {
        if (pharmacyJson != null && pharmacyJson!!.isNotEmpty()) {
            pharmacy = Gson().fromJson(pharmacyJson, Pharmacy::class.java)
        }
        rvCategories.layoutManager = LinearLayoutManager(context)
        rvCategories.setHasFixedSize(true)

        if (pharmacy == null) {
            currentFilter = OUT_OF_STOCK
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
            adapter = InventoryLimitCategoryAdapter(pharmacy!!, goodsCategories)
            rvCategories.adapter = adapter
            loadData()
        }
    }

    private fun loadData() {
        tvLimitFilterName.text = currentFilter.name
        goodsCategories.clear()
        adapter.notifyDataSetChanged()
        loadingIcon.visibility = View.VISIBLE
        // Xu ly va lay du lieu
        GetPharmacyGoodsCategoryProcessing(context!!, pharmacy!!.id).onFinished = {
            loadingIcon.visibility = View.GONE
            if (it != null) {
                goodsCategories.addAll(it.sortedBy { it.name })
                adapter.notifyDataSetChanged()
            } else {
                Toast.makeText(
                    context,
                    "Hiện tại bạn chưa có mục hàng nào!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun initViews(v: View) {
        rvCategories = v.findViewById(R.id.rvCategories)
        loadingIcon = v.findViewById(R.id.loadingIcon)
        tvLimitFilterName = v.findViewById(R.id.tvLimitFilterName)
        lnOpenFilterOptionsPicker = v.findViewById(R.id.lnOpenFilterOptionsPicker)
    }

    companion object {
        private var inventoryLimitFragment: InventoryLimitFragment? = null

        fun reload() {
            inventoryLimitFragment?.loadData()
        }

        data class InventoryFilter(val key: String, val name: String)

        val OUT_OF_STOCK = InventoryFilter("OUT_OF_STOCK", "Sản phẩm hết hàng")
        val UNDER_LIMIT = InventoryFilter("UNDER_LIMIT", "Sản phẩm sắp hết")
        val ABOVE_LIMIT = InventoryFilter("ABOVE_LIMIT", "Sản phẩm tồn nhiều")
        val NORMAL = InventoryFilter("NORMAL", "Trong ngưỡng")

        var currentFilter = OUT_OF_STOCK

        const val RELOAD_INVENTORY_LIMIT_GOODS_CATEGORY =
            "RELOAD_INVENTORY_LIMIT_GOODS_CATEGORY"
        const val ARG_PHARMACY_KEY = "ARG_PHARMACY_KEY"

        @JvmStatic
        fun newInstance(pharmacy: String) =
            InventoryLimitFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PHARMACY_KEY, pharmacy)
                }
            }
    }
}