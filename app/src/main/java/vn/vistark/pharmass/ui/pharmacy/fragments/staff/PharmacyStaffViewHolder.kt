package vn.vistark.pharmass.ui.pharmacy.fragments.staff

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import vn.vistark.pharmass.R
import vn.vistark.pharmass.core.model.PharmacyStaff
import vn.vistark.pharmass.utils.GlideUtils

class PharmacyStaffViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    val ivStaffAvatar: ImageView = v.findViewById(R.id.ivStaffAvatar)
    val tvStaffFullName: TextView = v.findViewById(R.id.tvStaffFullName)
    val tvStaffAddress: TextView = v.findViewById(R.id.tvStaffAddress)
    val tvStaffPhoneNumner: TextView = v.findViewById(R.id.tvStaffPhoneNumner)

    fun bind(staff: PharmacyStaff) {
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
        tvStaffAddress.text = if (staff.user.getAvatarFullAddress().isNotEmpty()) {
            staff.user.getAvatarFullAddress()
        } else {
            "Không có địa chỉ"
        }
        tvStaffAddress.isSelected = true
        tvStaffPhoneNumner.text = if (staff.user.phoneNumber.isNotEmpty()) {
            staff.user.phoneNumber
        } else {
            "Không có số điện thoại"
        }
        tvStaffPhoneNumner.isSelected = true
    }
}