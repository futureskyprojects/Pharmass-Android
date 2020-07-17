package vn.vistark.pharmass.ui.pharmacy_bill

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_good_updater.*
import kotlinx.android.synthetic.main.activity_pharmacy_bill.*
import kotlinx.android.synthetic.main.activity_pharmacy_bill.edtGoodsName
import kotlinx.android.synthetic.main.component_bill_patient_item.*
import kotlinx.android.synthetic.main.components_toolbar.*
import vn.vistark.pharmass.R
import vn.vistark.pharmass.core.api.response.SupplierSimplePharmacy
import vn.vistark.pharmass.core.constants.RequestCode
import vn.vistark.pharmass.core.model.MedicineCategory
import vn.vistark.pharmass.core.model.Supplier
import vn.vistark.pharmass.core.model.User
import vn.vistark.pharmass.ui.patient_picker.PatientPickerActivity
import vn.vistark.pharmass.utils.GlideUtils

class PharmacyBillActivity : AppCompatActivity() {

    var patient: User? = null

    val uriArrList = arrayListOf<Uri?>(null, null, null)
    var imageViewSelectedNumer = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pharmacy_bill)

        tvToolbarLabel.text = "Tạo mới đơn bán"
        inits()
        initEvents()
    }

    private fun initEvents() {
        ivBackButton.setOnClickListener {
            onBackPressed()
        }
        ivRemoveUserBtn.setOnClickListener {
            bind(null)
        }

        rlBillPatientItem.setOnClickListener {
            val intent = Intent(this, PatientPickerActivity::class.java)
            intent.putExtra(
                User::class.java.simpleName,
                Gson().toJson(patient)
            )
            startActivityForResult(intent, RequestCode.REQUEST_USER_PICKER_CODE)
            this.overridePendingTransition(0, 300)
        }
        ivDocsImage1.setOnClickListener {
            pickImage(1)
        }
        ivDocsImage2.setOnClickListener {
            pickImage(2)
        }
        ivDocsImage3.setOnClickListener {
            pickImage(3)
        }
    }

    private fun inits() {
        ivExpandAuthorInfomation.visibility = View.GONE
        ivBackButton.visibility = View.VISIBLE
        tvToolbarLabel.isSelected = true
        tvToolbarLabel.isAllCaps = false
        civUserAvatar.setImageResource(R.drawable.ic_verified)
        civUserAvatar.setPadding(
            tvToolbarLabel.paddingLeft + 20,
            tvToolbarLabel.paddingTop + 20,
            tvToolbarLabel.paddingRight,
            tvToolbarLabel.paddingBottom + 20
        )
    }

    fun bind(user: User?) {
        if (user != null) {
            ivRemoveUserBtn.visibility = View.VISIBLE
            tvPatientFullname.text = user.fullName
            tvPatientPhoneNumner.text = user.phoneNumber
            tvPatientAccountState.text = "Đang cập nhật..."
            GlideUtils.loadToImageViewWithPlaceHolder(
                ivPatientAvatar,
                user.getAvatarFullAddress(),
                R.drawable.no_avatar
            )
        } else {
            ivRemoveUserBtn.visibility = View.GONE
            tvPatientFullname.text = ""
            tvPatientPhoneNumner.text = "Chưa chọn ngườ mua"
            tvPatientAccountState.text = "Chưa xác nhận"
            ivPatientAvatar.setImageResource(R.drawable.no_avatar)
        }
        patient = user
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RequestCode.REQUEST_USER_PICKER_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val patientJson =
                data.getStringExtra(User::class.java.simpleName) ?: ""
            val p = Gson().fromJson(patientJson, User::class.java)
            if (p != null && patientJson.isNotEmpty()) {
                bind(p)
            }
        } else if (requestCode == RequestCode.REQUEST_PICK_PHOTO && resultCode == Activity.RESULT_OK) {
            val tempUri: Uri? = data?.data
            if (tempUri == null) {
                Toast.makeText(this, "Không lấy được ảnh chụp", Toast.LENGTH_SHORT).show()
                return
            }
            processingSelectedUri(tempUri)
        }
    }

    private fun pickImage(i: Int) {
        imageViewSelectedNumer = i
        val options =
            arrayOf<CharSequence>(
                "Chụp ảnh",
                "Ảnh từ thư viện",
                "Đóng"
            )
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Chọn hình ảnh")
        builder.setItems(options) { dialog, index ->
            when (index) {
                0 -> {
                    val values = ContentValues()
                    values.put(
                        MediaStore.Images.Media.TITLE,
                        "Vistark_${System.currentTimeMillis()}"
                    )
                    values.put(
                        MediaStore.Images.Media.DESCRIPTION,
                        "Write new app? contact projects.futuresky@gmail.com"
                    )
                    val takePicture =
                        Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    val tempUri = contentResolver.insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
                    )
                    if (tempUri == null) {
                        Toast.makeText(this, "Không lấy được ảnh chụp", Toast.LENGTH_SHORT).show()
                        return@setItems
                    }
                    processingSelectedUri(tempUri)
                    takePicture.putExtra(
                        MediaStore.EXTRA_OUTPUT, tempUri
                    )
                    startActivityForResult(takePicture, RequestCode.REQUEST_TAKE_PHOTO)
                }
                1 -> {
                    val pickPhoto = Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )
                    startActivityForResult(pickPhoto, RequestCode.REQUEST_PICK_PHOTO)
                }
                2 -> {
                    dialog.dismiss()
                }
            }
        }
        builder.show()
    }

    private fun processingSelectedUri(tempUri: Uri) {
        GlideUtils.loadToImageViewWithPlaceHolder(
            findViewById(
                resources.getIdentifier(
                    "ivDocsImage$imageViewSelectedNumer",
                    "id",
                    packageName
                )
            ),
            tempUri,
            R.drawable.no_image
        )
        uriArrList[imageViewSelectedNumer - 1] = tempUri
    }
}