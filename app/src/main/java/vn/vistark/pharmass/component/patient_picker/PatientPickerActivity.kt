package vn.vistark.pharmass.component.patient_picker

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
import kotlinx.android.synthetic.main.activity_patient_picker.*
import kotlinx.android.synthetic.main.activity_patient_picker.edtPhone
import kotlinx.android.synthetic.main.activity_patient_picker.lnMenuContainer
import kotlinx.android.synthetic.main.activity_patient_picker.loadingIcon
import kotlinx.android.synthetic.main.activity_patient_picker.rlHomeMenuRoot
import kotlinx.android.synthetic.main.activity_patient_picker.tvSelectMedicicecategory
import vn.vistark.pharmass.R
import vn.vistark.pharmass.core.model.MedicineCategory
import vn.vistark.pharmass.core.model.User
import vn.vistark.pharmass.processing.GetMedicineCategoryByNameProcessing
import vn.vistark.pharmass.processing.GetUserByPhoneProcessing

class PatientPickerActivity : AppCompatActivity() {
    val patients = ArrayList<User>()
    lateinit var adapter: PatientAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_picker)
        inits()
        initEvents()
        onTextChange()
        setResult(Activity.RESULT_CANCELED)
    }

    private fun inits() {
        val slideUpAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_up)
        lnMenuContainer.startAnimation(slideUpAnimation)

        rvPatient.setHasFixedSize(true)
        rvPatient.layoutManager = LinearLayoutManager(this)

        selectMedicineCategoryHintControl()

        adapter =
            PatientAdapter(patients)
        rvPatient.adapter = adapter

        adapter.onClicked = {
            val intent = Intent()
            intent.putExtra(User::class.java.simpleName, Gson().toJson(it))
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
    private fun initEvents() {
        rlHomeMenuRoot.setOnClickListener {
            rlHomeMenuRoot.setBackgroundColor(Color.TRANSPARENT)
            onBackPressed()
        }
    }

    private fun onTextChange() {
        edtPhone.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s != null) {
                    val name = s.toString()
                    if (name.length > 2) {
                        loadUserByPhone(name)
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    private fun selectMedicineCategoryHintControl() {
        if (patients.size > 0) {
            tvSelectMedicicecategory.visibility = View.VISIBLE
            rvPatient.visibility = View.VISIBLE
        } else {
            tvSelectMedicicecategory.visibility = View.GONE
            rvPatient.visibility = View.GONE
        }
    }

    private fun loadUserByPhone(phone: String) {
        loadingIcon.visibility = View.VISIBLE
        GetUserByPhoneProcessing(this, phone).onFinished = {
            loadingIcon.visibility = View.GONE
            if (it != null) {
                patients.clear()
                patients.addAll(it)
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
}