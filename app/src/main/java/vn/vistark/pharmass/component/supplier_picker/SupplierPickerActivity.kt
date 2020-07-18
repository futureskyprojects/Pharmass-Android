package vn.vistark.pharmass.component.supplier_picker

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_supplier_picker.*
import vn.vistark.pharmass.R
import vn.vistark.pharmass.core.api.response.SupplierSimplePharmacy
import vn.vistark.pharmass.core.model.Supplier
import vn.vistark.pharmass.processing.GetSupplierByNameProcessing
import vn.vistark.pharmass.ui.medicine_category_picker.SupplierPickerAdapter

class SupplierPickerActivity : AppCompatActivity() {

    val suppliers = ArrayList<Supplier>()

    lateinit var adapter: SupplierPickerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_supplier_picker)
        inits()
        initEvents()
        onTextChange()
        setResult(Activity.RESULT_CANCELED)
    }

    private fun onTextChange() {
        edtPhone.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s != null) {
                    val name = s.toString()
                    if (name.length > 2) {
                        loadSupplierByName(name)
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    private fun loadSupplierByName(name: String) {
        loadingIcon.visibility = View.VISIBLE
        GetSupplierByNameProcessing(this, name).onFinished = {
            loadingIcon.visibility = View.GONE
            if (it != null) {
                suppliers.clear()
                suppliers.addAll(it)
                adapter.notifyDataSetChanged()
            } else {
                Toast.makeText(
                    this,
                    "Không lấy được kết quả nào",
                    Toast.LENGTH_SHORT
                ).show()
            }
            selectMedicineCategoryHintControl()
        }
    }

    private fun initEvents() {
        rlHomeMenuRoot.setOnClickListener {
            rlHomeMenuRoot.setBackgroundColor(Color.TRANSPARENT)
            onBackPressed()
        }
    }

    private fun inits() {
        val slideUpAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_up)
        lnMenuContainer.startAnimation(slideUpAnimation)

        rvSupplier.setHasFixedSize(true)
        rvSupplier.layoutManager = LinearLayoutManager(this)

        selectMedicineCategoryHintControl()

        adapter = SupplierPickerAdapter(suppliers)
        rvSupplier.adapter = adapter

        adapter.onClicked = {
            val intent = Intent()
            var supplierSimplePharmacy = SupplierSimplePharmacy()
            supplierSimplePharmacy =
                supplierSimplePharmacy.copy(
                    id = it.id,
                    createdAt = it.createdAt,
                    updatedAt = it.updatedAt,
                    name = it.name,
                    address = it.address,
                    phoneNumber = it.phoneNumber,
                    pharmacy = it.pharmacy.id
                )
            intent.putExtra(Supplier::class.java.simpleName, Gson().toJson(supplierSimplePharmacy))
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    private fun selectMedicineCategoryHintControl() {
        if (suppliers.size > 0) {
            tvSelectMedicicecategory.visibility = View.VISIBLE
            rvSupplier.visibility = View.VISIBLE
        } else {
            tvSelectMedicicecategory.visibility = View.GONE
            rvSupplier.visibility = View.GONE
        }
    }
}