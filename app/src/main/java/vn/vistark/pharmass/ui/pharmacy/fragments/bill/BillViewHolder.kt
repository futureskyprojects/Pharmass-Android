package vn.vistark.pharmass.ui.pharmacy.fragments.bill

import android.media.Image
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.github.ybq.android.spinkit.SpinKitView
import vn.vistark.pharmass.R
import vn.vistark.pharmass.core.model.Bill
import vn.vistark.pharmass.core.model.User
import vn.vistark.pharmass.processing.GetBillItemByIdProcessing
import vn.vistark.pharmass.processing.GetUserByIdProcessing
import vn.vistark.pharmass.utils.DateTimeUtils
import vn.vistark.pharmass.utils.GlideUtils
import vn.vistark.pharmass.utils.NumberUtils

class BillViewHolder(v: View) : RecyclerView.ViewHolder(v) {

    val rlRoot: RelativeLayout = v.findViewById(R.id.rlRoot)
    val rlBillItemVisible: RelativeLayout = v.findViewById(R.id.rlBillItemVisible)
    val lnBillTotalPriceLayout: LinearLayout = v.findViewById(R.id.lnBillTotalPriceLayout)
    val ivBillImage: ImageView = v.findViewById(R.id.ivBillImage)
    val tvBillPrice: TextView = v.findViewById(R.id.tvBillPrice)
    val tvBillName: TextView = v.findViewById(R.id.tvBillName)
    val tvStaffFullName: TextView = v.findViewById(R.id.tvStaffFullName)
    val tvPatientFullname: TextView = v.findViewById(R.id.tvPatientFullname)
    val loadindForBillItem: SpinKitView = v.findViewById(R.id.loadindForBillItem)


    fun bind(bill: Bill) {
        tvBillPrice.text = "${NumberUtils.convertToVietNamCurrentcy(0.0)}đ"
        tvBillPrice.isSelected = true

        tvBillName.text = "#${bill.id}"
        val tempDate = DateTimeUtils.JsDateTimeStringToDate(bill.createdAt)
        if (tempDate != null) {
            tvBillName.text =
                "Đơn #${bill.id}, lúc ${DateTimeUtils.DateToString(
                    tempDate,
                    "HH:mm dd/MM/yyyy"
                )}"
            tvBillName.isSelected = true
        }
        // Tiến hành lấy danh sách bill-item và cộng
        updatePrice(bill)
        // Tiến hành lấy tên nhân viên bán
        getStaffFullName(bill.pharmacyStaff.user)
        // Tiến hành lấy tên khách hàng mua
        getUserFullName(bill.patient?.id ?: -1)

    }

    private fun getStaffFullName(userId: Int) {
        GetUserByIdProcessing(tvStaffFullName.context, userId).onFinished = {
            if (it == null || it.isEmpty()) {
                // Lấy lại
                getStaffFullName(userId)
            } else {
                tvStaffFullName.text = it.first().fullName
                GlideUtils.loadToImageViewWithPlaceHolder(
                    ivBillImage,
                    it.first().getAvatarFullAddress(),
                    R.drawable.no_image
                )
            }
        }
    }

    private fun getUserFullName(userId: Int) {
        if (userId <= 0) {
            tvPatientFullname.text = "Ẩn danh"
            return
        }
        GetUserByIdProcessing(tvPatientFullname.context, userId).onFinished = {
            if (it == null || it.isEmpty()) {
                tvPatientFullname.text = "Ẩn danh"
            } else {
                tvPatientFullname.text = it.first().fullName
            }
        }
    }

    fun updatePrice(bill: Bill, index: Int = 0) {
        if (index >= bill.simpleBillItems.size) {
            return
        }
        GetBillItemByIdProcessing(tvBillName.context, bill.simpleBillItems[index].id)
            .onFinished = {
            if (it != null) {
                val sum: Double =
                    tvBillPrice.text.toString().toDoubleOrNull() ?: 0.0 + it.goods.exportPrice
                tvBillPrice.text = "${NumberUtils.convertToVietNamCurrentcy(sum)}đ"

                updatePrice(bill, index + 1)
            } else {
                updatePrice(bill, index)
            }
        }
    }
}