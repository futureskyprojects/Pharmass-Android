package vn.vistark.pharmass.ui.create_medicine_category

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_add_medicine_category.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.vistark.pharmass.R
import vn.vistark.pharmass.core.api.APIUtils
import vn.vistark.pharmass.core.api.ErrorLibrary
import vn.vistark.pharmass.core.api.response.Error400Response
import vn.vistark.pharmass.core.api.response.Error401Response
import vn.vistark.pharmass.core.constants.Constants
import vn.vistark.pharmass.core.constants.vietnam.VietnamAdministrativeUnits
import vn.vistark.pharmass.core.model.MedicineCategory
import vn.vistark.pharmass.core.model.PharmacyStaff
import vn.vistark.pharmass.utils.DialogNotify
import java.io.InputStreamReader
import java.lang.Exception

class AddMedicineCategoryActivity : AppCompatActivity() {
    var error = 0
    var success = 0

    var medicineCategories: List<MedicineCategory> = emptyList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_medicine_category)

        // Đọc dữ liệu
        val inp = resources.openRawResource(R.raw.medicinecategories)
        medicineCategories =
            Gson().fromJson(
                InputStreamReader(inp),
                object : TypeToken<List<MedicineCategory>>() {}.type
            )
        // Set up progress
        pbProgress.max = medicineCategories.size
        pbProgress.progress = 0

        process()
    }

    private fun process(pos: Int = 0) {
        add(medicineCategories[pos]) {
            if (it) {
                success++
            } else {
                error++
            }
            pbProgress.progress++
            showStatus()
            if (pos < medicineCategories.size) {
                process(pos + 1)
            }
        }
    }

    fun showStatus() {
        tvProgressNumber.post {
            tvProgressNumber.text =
                "${pbProgress.progress}/${pbProgress.max} (${Math.round((pbProgress.progress.toFloat() / pbProgress.max.toFloat()) * 100) / 100}%)"

            tvError.text = "ERROR: $error"
            tvSuccess.text = "SUCCESS: $success"
        }
    }

    fun add(medicineCategory: MedicineCategory, onFinished: ((Boolean) -> Unit)) {
        tvMedicineCategoryName.post {
            tvMedicineCategoryName.text = medicineCategory.name
        }
        APIUtils.mAPIServices?.createMedicineCategory(medicineCategory)
            ?.enqueue(object : Callback<MedicineCategory> {
                override fun onFailure(call: Call<MedicineCategory>, t: Throwable) {
                    onFinished(false)
                }

                override fun onResponse(
                    call: Call<MedicineCategory>,
                    response: Response<MedicineCategory>
                ) {
                    if (response.isSuccessful) {
                        // Khi thực hiện thành công
                        onFinished(true)
                    } else {
                        onFinished(false)
                    }
                }
            })
    }
}