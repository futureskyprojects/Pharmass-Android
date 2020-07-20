package vn.vistark.pharmass.component.bill_detail

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_bill_detail.*
import kotlinx.android.synthetic.main.component_bill_patient_item.*
import kotlinx.android.synthetic.main.component_pharmacy_staff_item.*
import vn.vistark.pharmass.R
import vn.vistark.pharmass.component.goods_detail.GoodsDetailActivity
import vn.vistark.pharmass.component.user_basic_info.UserBasicInfoActivity
import vn.vistark.pharmass.core.model.Bill
import vn.vistark.pharmass.core.model.PharmacyStaff
import vn.vistark.pharmass.core.model.SimpleBillItem
import vn.vistark.pharmass.core.model.User
import vn.vistark.pharmass.processing.GetBillByIdProcessing
import vn.vistark.pharmass.processing.GetBillByPharmacyIdAndPatientIdProcessing
import vn.vistark.pharmass.processing.GetGoodsByIdProcessing
import vn.vistark.pharmass.processing.GetPharmacyStaffByIdProcessing
import vn.vistark.pharmass.ui.pharmacy.pharmacy_bill.BillItemAdapter
import vn.vistark.pharmass.utils.GlideUtils
import vn.vistark.pharmass.utils.NumberUtils
import vn.vistark.pharmass.utils.UrlUtils

class BillDetailActivity : AppCompatActivity() {

    companion object {
        fun start(
            context: Context,
            billId: Int
        ) {
            val intent = Intent(context, BillDetailActivity::class.java)
            intent.putExtra(
                BillDetailActivity::class.java.simpleName,
                billId
            )
            (context as AppCompatActivity).overridePendingTransition(0, 300)
            context.startActivity(intent)
        }
    }

    var billId: Int = -1

    var bill: Bill? = null

    val billItems = ArrayList<SimpleBillItem>()
    lateinit var adapter: BillItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bill_detail)
        setResult(Activity.RESULT_CANCELED)

        if (getData()) {
            showLoad()
            inits()
            initEvents()
            initRecyclerViewForBillItems()
            loadData()
        } else {
            Toast.makeText(
                this,
                "Lỗi trong quá trình truyền tải thông tin, vui lòng thử lại!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun initRecyclerViewForBillItems() {
        rvBillItems.setHasFixedSize(true)
        rvBillItems.layoutManager = LinearLayoutManager(this)

        adapter = BillItemAdapter(billItems)
        adapter.onItemLongClicked = { billItem ->
            GoodsDetailActivity.start(this, billItem.goods)
        }
        rvBillItems.adapter = adapter
    }

    private fun loadData() {
        GetBillByIdProcessing(this, billId).onFinished = { bill ->
            if (bill == null) {
                Toast.makeText(this, "Không thể lấy thông tin của đơn này!", Toast.LENGTH_SHORT)
                    .show()
            } else {
                this.bill = bill
                updateBaseView(bill)
                loadPharmacyStaff(bill.pharmacyStaff.id)
                showDetail()
            }
        }
    }

    private fun loadPharmacyStaff(id: Int) {
        GetPharmacyStaffByIdProcessing(this, id).onFinished = { pharmacyStaff ->
            bindStaff(pharmacyStaff)
        }
    }

    private fun updateBaseView(bill: Bill) {
        tvTheHint.text = "Chi tiết ${bill.getDesName()}"
        tvTheHint.isSelected = true
        tvConclude.text = bill.conclude
        tvNote.text = bill.note
        bindPatient(bill.patient)
        loadAndShowImageOfBill(bill.images)
        loadBillItemsToAdapter(bill.simpleBillItems)
    }

    private fun loadBillItemsToAdapter(simpleBillItems: List<SimpleBillItem>) {
        billItems.clear()
        billItems.addAll(simpleBillItems)
        billLayoutUpdater()
        adapter.notifyDataSetChanged()
    }

    private fun loadAndShowImageOfBill(images: List<String>) {
        if (images.isNotEmpty()) {
            for (i in images.indices) {
                try {
                    val v: ImageView = findViewById(
                        resources.getIdentifier(
                            "ivDocsImage$i",
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
            hsvDocImages.visibility = View.GONE
            tvDocImages.visibility = View.GONE
        }
    }

    private fun getData(): Boolean {
        billId = intent.getIntExtra(BillDetailActivity::class.java.simpleName, -1)
        return billId > 0
    }

    private fun inits() {
        val slideUpAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_up)
        lnMenuContainer.startAnimation(slideUpAnimation)
    }

    private fun initEvents() {
        rlHomeMenuRoot.setOnClickListener {
            rlHomeMenuRoot.setBackgroundColor(Color.TRANSPARENT)
            onBackPressed()
        }

        ivCloseButton.setOnClickListener {
            rlHomeMenuRoot.setBackgroundColor(Color.TRANSPARENT)
            onBackPressed()
        }
    }

    fun showLoad() {
        rlInfoDisplayArea.visibility = View.GONE
        skvLoadingIcon.visibility = View.VISIBLE
    }

    fun showDetail() {
        rlInfoDisplayArea.visibility = View.VISIBLE
        skvLoadingIcon.visibility = View.GONE
    }

    var totalMoney: Double = 0.0
    private fun billLayoutUpdater() {
        tvBillTotalMoney.text = "0 (VNĐ)"
        if (billItems.size > 0) {
            tvNoGoodsSelected.visibility = View.GONE
            billItems.forEach { simpleBillItem ->
                getGoodsPrice(simpleBillItem.goods)
            }
        } else {
            tvNoGoodsSelected.visibility = View.VISIBLE
        }
    }

    fun getGoodsPrice(goodId: Int) {
        GetGoodsByIdProcessing(this, goodId).onFinished = { goods ->
            if (goods != null) {
                totalMoney += goods.exportPrice
                tvBillTotalMoney.text = "${NumberUtils.convertToVietNamCurrentcy(totalMoney)} (VNĐ)"
            } else {
                getGoodsPrice(goodId)
            }
        }
    }

    private fun bindPatient(user: User?) {
        if (user != null) {
            rlBillPatientItem.setOnClickListener {
                UserBasicInfoActivity.start(this, user.id, true)
            }
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
                bill!!.pharmacyStaff.pharmacy,
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
            tvPatientFullname.text = ""
            tvPatientPhoneNumner.text = "Chưa chọn người mua"
            tvPatientAccountState.text = "Chưa xác nhận"
            ivPatientAvatar.setImageResource(R.drawable.no_avatar)
        }
    }

    fun bindStaff(staff: PharmacyStaff?) {
        if (staff != null) {
            rlRoot.setOnClickListener {
                UserBasicInfoActivity.start(this, staff.user.id, true)
            }
            GlideUtils.loadToImageViewWithPlaceHolder(
                ivStaffAvatar,
                staff.user.getAvatarFullAddress(),
                R.drawable.no_avatar
            )
            tvStaffFullName.text = if (staff.user.fullName.isNotEmpty()) {
                staff.user.fullName
            } else {
                "<Không có tên>"
            }
            tvStaffFullName.isSelected = true
            tvStaffAddress.text = staff.pharmacy.name
            tvStaffAddress.isSelected = true
            tvStaffPhoneNumner.text = if (staff.user.phoneNumber.isNotEmpty()) {
                staff.user.phoneNumber
            } else {
                "Không có số điện thoại"
            }
            tvStaffPhoneNumner.isSelected = true
        } else {
            incStaff.visibility = View.GONE
            tvNotFoundStaff.visibility = View.VISIBLE
        }
    }
}