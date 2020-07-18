package vn.vistark.pharmass.component.medicine_category_picker

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
import kotlinx.android.synthetic.main.activity_medicine_category_picker.*
import vn.vistark.pharmass.R
import vn.vistark.pharmass.core.model.MedicineCategory
import vn.vistark.pharmass.processing.GetMedicineCategoryByNameProcessing

class MedicineCategoryPickerActivity : AppCompatActivity() {

    val medicineCateories = ArrayList<MedicineCategory>()

    lateinit var adapter: MedicineCategoryAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medicine_category_picker)
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
                        println(name)
                        loadMedicineCategoryByName(name)
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    private fun loadMedicineCategoryByName(name: String) {
        loadingIcon.visibility = View.VISIBLE
        GetMedicineCategoryByNameProcessing(this, name).onFinished = {
            loadingIcon.visibility = View.GONE
            if (it != null) {
                medicineCateories.clear()
                medicineCateories.addAll(it)
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

        adapter =
            MedicineCategoryAdapter(
                medicineCateories
            )
        rvSupplier.adapter = adapter

        adapter.onClicked = {
            val intent = Intent()
            intent.putExtra(MedicineCategory::class.java.simpleName, Gson().toJson(it))
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    private fun selectMedicineCategoryHintControl() {
        if (medicineCateories.size > 0) {
            tvSelectMedicicecategory.visibility = View.VISIBLE
            rvSupplier.visibility = View.VISIBLE
        } else {
            tvSelectMedicicecategory.visibility = View.GONE
            rvSupplier.visibility = View.GONE
        }
    }
}