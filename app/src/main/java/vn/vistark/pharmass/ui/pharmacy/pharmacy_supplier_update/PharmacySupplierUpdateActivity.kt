package vn.vistark.pharmass.ui.pharmacy.pharmacy_supplier_update

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_pharmacy_supplier_update.*
import kotlinx.android.synthetic.main.components_toolbar.*
import vn.vistark.pharmass.R
import vn.vistark.pharmass.core.api.response.PharmacySimpleOwner
import vn.vistark.pharmass.core.model.Pharmacy
import vn.vistark.pharmass.core.model.Supplier
import vn.vistark.pharmass.databinding.ActivityPharmacySupplierUpdateBinding
import vn.vistark.pharmass.processing.CreateOrUpdateSupplierProcessing
import vn.vistark.pharmass.ui.pharmacy.fragments.supplier.SupplierFragment
import vn.vistark.pharmass.utils.DialogNotify

class PharmacySupplierUpdateActivity : AppCompatActivity() {

    lateinit var binding: ActivityPharmacySupplierUpdateBinding

    var pharmacy: Pharmacy? = null
    var supplier: Supplier? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pharmacy_supplier_update)
        setResult(Activity.RESULT_CANCELED)
        if (getData()) {
            inits()
            initEvents()
        } else {
            Toast.makeText(
                this,
                "Gặp lỗi trong quá trình truyền tải dữ liệu, vui lòng thử lại!",
                Toast.LENGTH_SHORT
            ).show()
            finish()
        }
    }

    private fun inits() {
        ivExpandAuthorInfomation.visibility = View.GONE
        ivBackButton.visibility = View.VISIBLE
        tvToolbarLabel.text = "Cập nhật nhà cung cấp"
        tvToolbarLabel.setPadding(
            tvToolbarLabel.paddingLeft,
            tvToolbarLabel.paddingTop,
            tvToolbarLabel.paddingRight,
            tvToolbarLabel.paddingBottom
        )
        civUserAvatar.visibility = View.GONE
    }

    private fun initEvents() {
        ivBackButton.setOnClickListener {
            onBackPressed()
        }
        btnConfirm.setOnClickListener {
            if (binding.supplier!!.name.isEmpty()) {
                DialogNotify.missingInput(this, "Vui lòng nhập tên nhà cung cấp")
            } else if (binding.supplier!!.phoneNumber.isEmpty()) {
                DialogNotify.missingInput(this, "Vui lòng nhập số điện thoại của nhà cung cấp")
            } else if (binding.supplier!!.address.isEmpty()) {
                DialogNotify.missingInput(this, "Vui lòng nhập địa chỉ của nhà cung cấp")
            } else {
                CreateOrUpdateSupplierProcessing(this, binding.supplier!!)
                    .onFinished = { s ->
                    if (s != null) {
                        Toast.makeText(this, "Thêm nhà cung cấp thành công!", Toast.LENGTH_SHORT)
                            .show()
                        setResult(Activity.RESULT_OK)
                        finish()
                    } else {
                        DialogNotify.error(this, "Thêm nhà cung cấp chưa được, vui lòng thử lại!")
                    }
                }
            }
        }
    }

    private fun getData(): Boolean {
        val pharmacyJson = intent.getStringExtra(Pharmacy::class.java.simpleName) ?: ""
        pharmacy = Gson().fromJson(pharmacyJson, Pharmacy::class.java)

        val supplierJson = intent.getStringExtra(Supplier::class.java.simpleName) ?: ""
        supplier = Gson().fromJson(supplierJson, Supplier::class.java)

        if (pharmacyJson.isNotEmpty() && pharmacy != null) {
            if (supplierJson.isNotEmpty() && supplier != null) {
                binding.supplier = supplier
            } else {
                binding.supplier = Supplier()
                binding.supplier!!.pharmacy = PharmacySimpleOwner.fromPharmacy(pharmacy!!)
            }
            return true
        } else {
            return false
        }
    }
}