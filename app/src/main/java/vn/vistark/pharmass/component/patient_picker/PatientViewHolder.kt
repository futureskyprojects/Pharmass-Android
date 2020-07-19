package vn.vistark.pharmass.component.patient_picker

import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import vn.vistark.pharmass.R
import vn.vistark.pharmass.core.model.Pharmacy
import vn.vistark.pharmass.core.model.User
import vn.vistark.pharmass.processing.GetBillByPharmacyIdAndPatientIdProcessing
import vn.vistark.pharmass.utils.GlideUtils

class PatientViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    val rlBillPatientItem: RelativeLayout = v.findViewById(R.id.rlBillPatientItem)
    val tvPatientFullname: TextView = v.findViewById(R.id.tvPatientFullname)
    val tvPatientPhoneNumner: TextView = v.findViewById(R.id.tvPatientPhoneNumner)
    val tvPatientAccountState: TextView = v.findViewById(R.id.tvPatientAccountState)
    val ivPatientAvatar: ImageView = v.findViewById(R.id.ivPatientAvatar)

    fun bind(pharmacy: Pharmacy, user: User) {
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
    }
}