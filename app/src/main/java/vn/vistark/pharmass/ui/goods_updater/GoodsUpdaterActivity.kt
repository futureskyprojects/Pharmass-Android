package vn.vistark.pharmass.ui.goods_updater

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_good_updater.*
import kotlinx.android.synthetic.main.activity_good_updater.ivGoodsImage1
import kotlinx.android.synthetic.main.activity_good_updater.ivGoodsImage2
import kotlinx.android.synthetic.main.activity_good_updater.ivGoodsImage3
import kotlinx.android.synthetic.main.activity_good_updater.tvGoodsName
import kotlinx.android.synthetic.main.activity_good_updater.tvManufacturerCountry
import kotlinx.android.synthetic.main.activity_good_updater.tvPacking
import kotlinx.android.synthetic.main.activity_good_updater.tvUnit
import kotlinx.android.synthetic.main.activity_goods_detail.*
import kotlinx.android.synthetic.main.components_toolbar.*
import vn.vistark.pharmass.R
import vn.vistark.pharmass.core.api.response.GoodsCategorySimplePharmacy
import vn.vistark.pharmass.core.api.response.PharmacySimpleOwner
import vn.vistark.pharmass.core.api.response.SupplierSimplePharmacy
import vn.vistark.pharmass.core.constants.Constants
import vn.vistark.pharmass.core.constants.RequestCode
import vn.vistark.pharmass.core.model.*
import vn.vistark.pharmass.databinding.ActivityGoodUpdaterBinding
import vn.vistark.pharmass.processing.CreateOrUpdatePharmacyGoodsInCategoryProcessing
import vn.vistark.pharmass.processing.UserUploadImageProcessing
import vn.vistark.pharmass.component.barcode_scanner.BarcodeScannerActivity
import vn.vistark.pharmass.component.medicine_category_picker.MedicineCategoryPickerActivity
import vn.vistark.pharmass.component.supplier_picker.SupplierPickerActivity
import vn.vistark.pharmass.utils.DialogNotify
import vn.vistark.pharmass.utils.GlideUtils
import vn.vistark.pharmass.utils.UrlUtils

class GoodsUpdaterActivity : AppCompatActivity() {

    var isCreateNewGoods: Boolean = true

    var pharmacyJson: String = ""
    var goodsCategoryJson: String = ""

    lateinit var pharmacy: Pharmacy
    lateinit var goodsCategory: GoodsCategory

    val uriArrList = arrayListOf<Uri?>(null, null, null)

    var imageViewSelectedNumer = -1

    var medicineCategory: MedicineCategory? = null

    lateinit var binding: ActivityGoodUpdaterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_good_updater)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_good_updater)
        binding.requestCreateGoods = Goods()

        setResult(Activity.RESULT_CANCELED)

        if (!getPassingData()) {
            // Bỏ qua các phương thức khởi tạo bên dưới và kết thúc màn hình này
            finish()
            return
        }

        // Fill image list
        for (i in 0..(3 - binding.requestCreateGoods!!.images.size))
            binding.requestCreateGoods!!.images += ""

        edtSupplier.inputType = InputType.TYPE_NULL
        // Xử lý phân biệt đối với thuốc và các sản phẩm không phải là thuốc
        if (goodsCategory.code == Constants.MEDICINE_CATEGORY_CODE) {
            // Nếu danh mục sản phẩm là thuốc
            tvUnit.visibility = View.GONE
            edtUnit.visibility = View.GONE

            tvManufacturerCountry.visibility = View.GONE
            edtManufacturerCountry.visibility = View.GONE

            tvPacking.visibility = View.GONE
            edtPacking.visibility = View.GONE

            tvGoodsName.text = "Chọn tên thuốc (Bắt buộc)"
            edtGoodsName.hint = "Chọn tên thuốc"
            edtGoodsName.inputType = InputType.TYPE_NULL
            edtGoodsName.setOnClickListener {
                val intent = Intent(this, MedicineCategoryPickerActivity::class.java)
                intent.putExtra(
                    MedicineCategory::class.java.simpleName,
                    Gson().toJson(medicineCategory)
                )
                startActivityForResult(intent, RequestCode.REQUEST_MEDICINE_CATEGORY_CODE)
                this.overridePendingTransition(0, 300)
            }
        } else {
            // Nếu danh mục sản phẩm khôn phải thuốc
        }

        inits()
        initEvents()
    }

    private fun getPassingData(): Boolean {
        // Thử lấy hàng nếu được truyền
        val goodsJson = intent.getStringExtra(Goods::class.java.simpleName) ?: ""
        val tempGoods = Gson().fromJson(goodsJson, Goods::class.java)
        if (goodsJson.isNotEmpty() && tempGoods != null) {
            isCreateNewGoods = false
            binding.requestCreateGoods = tempGoods
            if (binding.requestCreateGoods!!.supplier != null) {
                edtSupplier.setText(binding.requestCreateGoods!!.supplier!!.name)
            }
            loadExistsImageOfGoods(binding.requestCreateGoods!!.images)
        }

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
        // Tiến hành lấy thông tin danh mục loại sản phẩm
        goodsCategoryJson = intent.getStringExtra(GoodsCategory::class.java.simpleName) ?: ""
        goodsCategory = Gson().fromJson(goodsCategoryJson, GoodsCategory::class.java)
        if (goodsCategoryJson.isEmpty()) {
            Toast.makeText(
                this,
                "Không thể lấy được thông tin danh mục loại sản phẩm truyền vào",
                Toast.LENGTH_SHORT
            ).show()
            finish()
            return false
        }
        if (isCreateNewGoods) {
            // Gán vào binding
            binding.requestCreateGoods!!.pharmacy = PharmacySimpleOwner.fromPharmacy(pharmacy)
            binding.requestCreateGoods!!.goodsCategory =
                GoodsCategorySimplePharmacy.fromGoodsCategory(goodsCategory)
        }
        return true
    }

    private fun initEvents() {
        ivBackButton.setOnClickListener {
            onBackPressed()
        }
        ivGoodsImage1.setOnClickListener {
            pickImage(1)
        }
        ivGoodsImage2.setOnClickListener {
            pickImage(2)
        }
        ivGoodsImage3.setOnClickListener {
            pickImage(3)
        }
        btnScanBarcode.setOnClickListener {
            val intent = Intent(this, BarcodeScannerActivity::class.java)
            startActivityForResult(intent, RequestCode.REQUEST_BARCODE_SCANNER)
        }

        btnGoodsUpdaterConfirm.setOnClickListener {
            val validateMessage = binding.requestCreateGoods!!.validate()
            if (validateMessage.isEmpty()) {
                // Không có lỗi nào trong quá trình nhập form, tiến hành upload
                updateProcessing(0)
            } else {
                DialogNotify.missingInput(this, validateMessage)
            }
        }

        edtSupplier.setOnClickListener {
            val intent = Intent(this, SupplierPickerActivity::class.java)
            intent.putExtra(
                MedicineCategory::class.java.simpleName,
                Gson().toJson(binding.requestCreateGoods!!.supplier)
            )
            intent.putExtra(
                Pharmacy::class.java.simpleName,
                pharmacyJson
            )
            startActivityForResult(intent, RequestCode.REQUEST_SUPPLIER_PICKER_CODE)
            this.overridePendingTransition(0, 300)
        }
    }

    private fun updateProcessing(i: Int) {
        if (i < uriArrList.size) {
            // Tiến hành upload ảnh
            if (uriArrList[i] != null) {
                UserUploadImageProcessing(this, uriArrList[i]!!).apply {
                    onFinished = {
                        if (it != null) {
                            val tempArray = binding.requestCreateGoods!!.images.toTypedArray()
                            tempArray[i] = it
                            binding.requestCreateGoods!!.images = tempArray.toList()
//                                binding.requestCreateGoods!!.images =
//                                binding.requestCreateGoods!!.images.plus(it)
                        }
                        updateProcessing(i + 1)
                    }
                    execute()
                }
            } else {
                updateProcessing(i + 1)
            }
        } else {
            // Tiến hành cập nhật sản phẩm
            CreateOrUpdatePharmacyGoodsInCategoryProcessing(this, binding.requestCreateGoods!!)
                .onFinished = {
                if (it != null) {
                    val data = Intent()
                    data.putExtra(Goods::class.java.simpleName, Gson().toJson(it))
                    setResult(Activity.RESULT_OK, data)
                    finish()
                } else {
                    Toast.makeText(
                        this,
                        "Có lỗi khi lấy thông tin sản phẩm vừa cập nhật, vui lòng thử lại",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun inits() {
        ivExpandAuthorInfomation.visibility = View.GONE
        ivBackButton.visibility = View.VISIBLE
        tvToolbarLabel.text = "Cập nhật sản phẩm"
        tvToolbarLabel.setPadding(
            tvToolbarLabel.paddingLeft,
            tvToolbarLabel.paddingTop,
            tvToolbarLabel.paddingRight,
            tvToolbarLabel.paddingBottom
        )
        civUserAvatar.visibility = View.GONE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RequestCode.REQUEST_MEDICINE_CATEGORY_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val medicineCategoryJson =
                data.getStringExtra(MedicineCategory::class.java.simpleName) ?: ""
            medicineCategory = Gson().fromJson(medicineCategoryJson, MedicineCategory::class.java)
            if (medicineCategory != null && medicineCategoryJson.isNotEmpty()) {
                edtGoodsName.setText(medicineCategory!!.name)
                if (medicineCategory!!.unit.isNotEmpty()) {
                    edtUnit.setText(medicineCategory!!.unit)
                } else {
                    edtUnit.visibility = View.VISIBLE
                    tvUnit.visibility = View.VISIBLE
                }
                if (medicineCategory!!.manufacturerCountry.isNotEmpty()) {
                    edtManufacturerCountry.setText(medicineCategory!!.manufacturerCountry)
                } else {
                    edtManufacturerCountry.visibility = View.VISIBLE
                    tvManufacturerCountry.visibility = View.VISIBLE
                }
                if (medicineCategory!!.packing.isNotEmpty()) {
                    edtPacking.setText(medicineCategory!!.packing)
                } else {
                    edtPacking.visibility = View.VISIBLE
                    tvPacking.visibility = View.VISIBLE
                }
                // Gán danh mục thuốc lấy được vào binding
                binding.requestCreateGoods!!.medicineCategory = medicineCategory
            }
        } else if (requestCode == RequestCode.REQUEST_PICK_PHOTO && resultCode == Activity.RESULT_OK) {
            val tempUri: Uri? = data?.data
            if (tempUri == null) {
                Toast.makeText(this, "Không lấy được ảnh chụp", Toast.LENGTH_SHORT).show()
                return
            }
            processingSelectedUri(tempUri)
        } else if (requestCode == RequestCode.REQUEST_SUPPLIER_PICKER_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val supplierJson =
                data.getStringExtra(Supplier::class.java.simpleName) ?: ""
            binding.requestCreateGoods!!.supplier =
                Gson().fromJson(supplierJson, SupplierSimplePharmacy::class.java)
            if (binding.requestCreateGoods!!.supplier != null && supplierJson.isNotEmpty()) {
                edtSupplier.setText(binding.requestCreateGoods!!.supplier!!.name)
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
                    "ivGoodsImage$imageViewSelectedNumer",
                    "id",
                    packageName
                )
            ),
            tempUri,
            R.drawable.no_image
        )
        uriArrList[imageViewSelectedNumer - 1] = tempUri
    }

    private fun loadExistsImageOfGoods(images: List<String>) {
        if (images.isNotEmpty()) {
            for (i in images.indices) {
                try {
                    val v: ImageView = findViewById(
                        resources.getIdentifier(
                            "ivGoodsImage$i",
                            "id",
                            packageName
                        )
                    )
                    GlideUtils.loadToImageViewWithPlaceHolder(
                        v,
                        UrlUtils.truePathOfMyServer(images[i]),
                        R.drawable.no_image
                    )
                    v.visibility = View.VISIBLE
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else {
            tvGoodsDetailImageLabel.visibility = View.GONE
            hsvGoodsDetailImages.visibility = View.GONE
        }
    }
}