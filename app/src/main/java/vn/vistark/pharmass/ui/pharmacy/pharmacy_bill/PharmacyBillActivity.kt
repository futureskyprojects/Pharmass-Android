package vn.vistark.pharmass.ui.pharmacy.pharmacy_bill

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
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_pharmacy_bill.*
import kotlinx.android.synthetic.main.component_bill_patient_item.*
import kotlinx.android.synthetic.main.components_toolbar.*
import vn.vistark.pharmass.R
import vn.vistark.pharmass.component.bill_item_updater.BillItemUpdaterActivity
import vn.vistark.pharmass.component.goods_detail.GoodsDetailActivity
import vn.vistark.pharmass.component.goods_picker.GoodsPickerActivity
import vn.vistark.pharmass.core.constants.RequestCode
import vn.vistark.pharmass.component.patient_picker.PatientPickerActivity
import vn.vistark.pharmass.component.user_basic_info.UserBasicInfoActivity
import vn.vistark.pharmass.core.constants.Constants
import vn.vistark.pharmass.core.model.*
import vn.vistark.pharmass.databinding.ActivityPharmacyBillBinding
import vn.vistark.pharmass.processing.*
import vn.vistark.pharmass.utils.DialogNotify
import vn.vistark.pharmass.utils.GlideUtils
import vn.vistark.pharmass.utils.NumberUtils

class PharmacyBillActivity : AppCompatActivity() {

    lateinit var binding: ActivityPharmacyBillBinding

    var pharmacyJson: String = ""
    lateinit var pharmacy: Pharmacy

    val uriArrList = arrayListOf<Uri?>(null, null, null)
    var imageViewSelectedNumer = -1

    val billItems = ArrayList<SimpleBillItem>()
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
            intent.putExtra(
                Pharmacy::class.java.simpleName,
                Gson().toJson(pharmacy)
            )
            startActivityForResult(intent, RequestCode.REQUEST_USER_PICKER_CODE)
            this.overridePendingTransition(0, 300)
        }

        rlBillPatientItem.setOnLongClickListener {
            if (binding.bill!!.patient != null) {
                UserBasicInfoActivity.start(this, binding.bill!!.patient!!.id, true)
            } else {
                Toast.makeText(this, "Hiện người mua đang ở chế độ ẩn danh", Toast.LENGTH_SHORT)
                    .show()
            }
            return@setOnLongClickListener true
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
        civUserAvatar.setOnClickListener {
            if (billItems.isEmpty()) {
                DialogNotify.warning(this, "Đơn bán này chưa có sản phẩm nào được chọn")
            } else if (binding.bill!!.conclude.isEmpty()) {
                DialogNotify.missingInput(this, "Vui lòng cung cấp chẩn đoán cho người mua")
            } else if (binding.bill!!.patient == null) {
                // Có chắc ẩn danh người mua này
                SweetAlertDialog(this).apply {
                    titleText =
                        "Vẫn chưa chọn khách hàng mua, việc này sẽ khó phân loại khách hàng sau này. Bạn vẫn muốn tiếp tục tạo đơn bán?"
                    contentText = "THIẾU NGƯỜI MUA"
                    setConfirmButton("Tiếp tục") {
                        it.dismiss()
                        postBillItems()
                    }

                    setCancelButton("Quay lại") {
                        it.dismiss()
                    }
                    show()
                }
            } else {
                postBillItems()
            }
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

        adapter = BillItemAdapter(
            billItems
        )
        adapter.onItemLongClicked = { billItem ->
            SweetAlertDialog(this).apply {
                titleText = "Thực sụ muốn xóa sản phẩm này khỏi danh sách hàng khách mua?"
                contentText = "Xóa sản phẩm"
                setConfirmButton("Xóa") {
                    it.dismiss()
                    val temp = billItems.filter { bi -> bi.goods != billItem.goods }
                    billItems.clear()
                    billItems.addAll(temp)

                    billLayoutUpdater()
                }
                setCancelButton("Không xóa") {
                    it.dismiss()
                }
                show()
            }
        }
        adapter.onClicked = {
            GoodsDetailActivity.start(this, it.goods, true)
        }
        rvBillItems.adapter = adapter

        initStaffForThisBill()
    }

    private fun initStaffForThisBill() {
        GetPharmacyStaffByPharmacyProcessing(this, pharmacy.id, Constants.user.id).onFinished =
            { pharmacyStaffs ->
                if (pharmacyStaffs != null && pharmacyStaffs.size > 0) {
                    binding.bill!!.pharmacyStaff.from(pharmacyStaffs.first())
                } else {
                    Toast.makeText(
                        this,
                        "Không thể xác định vai trò của bạn trong nhà thuốc này. Vui lòng thử lại!",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
            }
    }

    private fun billLayoutUpdater() {
        if (billItems.size > 0) {
            tvNoGoodsSelected.visibility = View.GONE
            var totalMoney = 0.0
            billItems.forEach {
                totalMoney += it.tempGoods!!.exportPrice
            }
            tvBillTotalMoney.text = "${NumberUtils.convertToVietNamCurrentcy(totalMoney)} (VNĐ)"
        } else {
            tvNoGoodsSelected.visibility = View.VISIBLE
            tvBillTotalMoney.text = "0 (VNĐ)"
        }
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
            GetBillByPharmacyIdAndPatientIdProcessing(
                tvPatientAccountState.context,
                pharmacy.id,
                user.id
            ).onFinished = { bills ->
                if (bills == null || bills.isEmpty()) {
                    tvPatientAccountState.text = "Khách hàng mới"
                } else {
                    var totalGoodsBuy = 0
                    repeat(bills.size) { i ->
                        totalGoodsBuy += bills[i].simpleBillItems.size
                    }
                    tvPatientAccountState.text =
                        "Đã có ${bills.size} đơn mua (${totalGoodsBuy} sản phẩm)"
                }
            }
        } else {
            ivRemoveUserBtn.visibility = View.GONE
            tvPatientFullname.text = ""
            tvPatientPhoneNumner.text = "Chưa chọn người mua"
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
                this.overridePendingTransition(0, 300)
                startActivityForResult(intent, RequestCode.REQUEST_BILL_ITEM_DETAILS_CODE)
            }
        } else if (requestCode == RequestCode.REQUEST_BILL_ITEM_DETAILS_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val billItemJson =
                data.getStringExtra(SimpleBillItem::class.java.simpleName) ?: ""
            val billItem = Gson().fromJson(billItemJson, SimpleBillItem::class.java)
            if (billItem != null && billItemJson.isNotEmpty()) {
                // Cập nhật sản phẩm vào danh sách
                val bi = billItems.filter { bi -> bi.goods == billItem.goods }
                if (bi.isNotEmpty()) {
                    DialogNotify.error(this, "Sản phẩm đã tồn tại")
                } else {
                    billItems.add(billItem)
                    adapter.notifyDataSetChanged()
                }
                billLayoutUpdater()
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

    //=========== Khu vực xử lý và gửi dữ liệu đơn bán lên server ==========//
    private fun postBillItems(index: Int = 0) {
        // Nếu chưa chọn sản phẩm nào để bán thì tiến hàn thông báo và trở lên
        if (billItems.size < 0) {
            DialogNotify.warning(this, "Đơn bán này chưa có sản phẩm nào được chọn")
            return
        }

        if (index >= billItems.size) {
            // Đến đây, tức là đã cập nhật tất cả các sản phẩm vào đơn bán.
            // Tiến hành gửi ảnh đơn bán nếu có
            updateImageProcessing()
        } else {
            // Nếu đã có sản phẩm, tiến hành thêm và cập nhật vào đơn bán hiện tại từng cái một
            CreateOrUpdateBillItemProcessing(this, billItems[index]).onFinished = {
                if (it == null) {
                    // Tiến hành cập nhật lại sản phẩm lỗi vừa rồi
                    postBillItems(index)
                } else {
                    if (billItems[index].tempGoods != null) {
                        binding.bill!!.simpleBillItems += SimpleBillItem().copy(
                            id = it.id,
                            dosage = it.dosage,
                            direction = it.direction,
                            createdAt = it.createdAt,
                            updatedAt = it.updatedAt,
                            goods = it.goods.id
                        )

                        reduceGoodsAmount(
                            billItems[index].tempGoods!!,
                            it.dosage.toFloat()
                        ) { zGoods ->
                            if (zGoods != null) {
                                // Cập nhật sản phẩm tiếp theo vào đơn bán
                                postBillItems(index + 1)
                            } else {
                                Toast.makeText(
                                    this,
                                    "Không giảm số lượng thuốc trong kho xuống được",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } else {
                        Toast.makeText(
                            this,
                            "Không lấy được thông tin của sản phẩm cần giảm số lượng",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    fun reduceGoodsAmount(goods: Goods, forSellAmount: Float, finish: ((Goods?) -> Unit)) {
        goods.amount -= forSellAmount
        if (goods.amount < 0)
            goods.amount = 0F
        CreateOrUpdatePharmacyGoodsInCategoryProcessing(this, goods)
            .onFinished = {
            finish(it)
        }
    }

    private fun updateImageProcessing(i: Int = 0) {
        if (i < uriArrList.size) {
            if (uriArrList[i] != null) {
                UserUploadImageProcessing(this, uriArrList[i]!!).apply {
                    onFinished = {
                        if (it != null) {
                            binding.bill!!.images += it
                        }
                        updateImageProcessing(i + 1)
                    }
                    execute()
                }
            } else {
                updateImageProcessing(i + 1)
            }
        } else {
            updateBill()
        }
    }

    private fun updateBill() {
        CreateBillProcessing(this, binding.bill!!).onFinished = {
            if (it != null) {
                Toast.makeText(this, "Cập nhật đơn bán thành công", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                DialogNotify.error(this, "Cập nhật đơn bán không thành công!")
            }
        }
    }
}