package vn.vistark.pharmass.ui.pharmacy_bill

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_pharmacy_bill.*
import kotlinx.android.synthetic.main.component_bill_patient_item.*
import kotlinx.android.synthetic.main.components_toolbar.*
import vn.vistark.pharmass.R
import vn.vistark.pharmass.component.bill_item_updater.BillItemUpdaterActivity
import vn.vistark.pharmass.component.goods_picker.GoodsPickerActivity
import vn.vistark.pharmass.component.medicine_category_picker.MedicineCategoryPickerActivity
import vn.vistark.pharmass.core.constants.RequestCode
import vn.vistark.pharmass.databinding.ActivityPharmacyBillBinding
import vn.vistark.pharmass.component.patient_picker.PatientPickerActivity
import vn.vistark.pharmass.core.model.*
import vn.vistark.pharmass.utils.GlideUtils

class PharmacyBillActivity : AppCompatActivity() {

    lateinit var binding: ActivityPharmacyBillBinding

    var pharmacyJson: String = ""
    lateinit var pharmacy: Pharmacy

    val uriArrList = arrayListOf<Uri?>(null, null, null)
    var imageViewSelectedNumer = -1

    val billItems = ArrayList<BillItem>()
    lateinit var adapter: BillItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pharmacy_bill)
//        setContentView(R.layout.activity_pharmacy_bill)
        binding.bill = Bill()


        if (getPassingData()) {
            tvToolbarLabel.text = "Tạo mới đơn bán"
            inits()
            initEvents()
        } else {
            Toast.makeText(
                this,
                "Không nhận diện được nhà thuốc hiện đang thao tác",
                Toast.LENGTH_SHORT
            ).show()
            finish()
        }
    }

    private fun getPassingData(): Boolean {
        // Tiến hành lấy thông tin nhà thuốc
        pharmacyJson = intent.getStringExtra(Pharmacy::class.java.simpleName) ?: ""
        pharmacy = Gson().fromJson(pharmacyJson, Pharmacy::class.java)
        if (pharmacyJson.isEmpty()) {
            Toast.makeText(
                this,
                "Không lấy được thông tin nhà thuốc truyền vào",
                Toast.LENGTH_SHORT
            ).show()
            finish()
            return false
        }
        return true
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
                Gson().toJson(binding.bill!!.patient)
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
        lnAddNewGoodsToCard.setOnClickListener {
            val intent = Intent(this, GoodsPickerActivity::class.java)
            intent.putExtra(
                Pharmacy::class.java.simpleName,
                Gson().toJson(pharmacy)
            )
            startActivityForResult(intent, RequestCode.REQUEST_GOODS_PICKER_CODE)
            this.overridePendingTransition(0, 300)
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

        rvBillItems.setHasFixedSize(true)
        rvBillItems.layoutManager = LinearLayoutManager(this)

        adapter = BillItemAdapter(billItems)
        rvBillItems.adapter = adapter
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
        binding.bill!!.patient = user
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
        } else if (requestCode == RequestCode.REQUEST_GOODS_PICKER_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val goodsJson =
                data.getStringExtra(Goods::class.java.simpleName) ?: ""
            val g = Gson().fromJson(goodsJson, Goods::class.java)
            if (g != null && goodsJson.isNotEmpty()) {
                // Tiến hành tạo Bill Item cho sản phẩm
                val intent = Intent(this, BillItemUpdaterActivity::class.java)
                intent.putExtra(
                    Goods::class.java.simpleName,
                    Gson().toJson(g)
                )
                startActivityForResult(intent, RequestCode.REQUEST_BILL_ITEM_DETAILS_CODE)
                this.overridePendingTransition(0, 300)
            }
        } else if (requestCode == RequestCode.REQUEST_BILL_ITEM_DETAILS_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val billItemJson =
                data.getStringExtra(BillItem::class.java.simpleName) ?: ""
            val billItem = Gson().fromJson(billItemJson, BillItem::class.java)
            if (billItem != null && billItemJson.isNotEmpty()) {
                // Cập nhật sản phẩm vào danh sách
                billItems.add(billItem)
                adapter.notifyDataSetChanged()
            }
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