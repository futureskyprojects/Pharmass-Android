package vn.vistark.pharmass.ui.pharmacy_ware_house

import android.media.Image
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import vn.vistark.pharmass.R
import vn.vistark.pharmass.core.model.Goods
import vn.vistark.pharmass.utils.GlideUtils
import vn.vistark.pharmass.utils.NumberUtils
import vn.vistark.pharmass.utils.NumberUtils.Companion.convertToVietNamCurrentcy
import vn.vistark.pharmass.utils.NumberUtils.Companion.removeUnMean

class GoodsViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    val rlRoot: RelativeLayout = v.findViewById(R.id.rlRoot)
    val tvGoodsName: TextView = v.findViewById(R.id.tvGoodsName)
    val tvAmount: TextView = v.findViewById(R.id.tvAmount)
    val tvLimit: TextView = v.findViewById(R.id.tvLimit)
    val ivGoodsImage: ImageView = v.findViewById(R.id.ivGoodsImage)
    val tvGoodsPrice: TextView = v.findViewById(R.id.tvGoodsPrice)

    fun bind(goods: Goods) {
        tvGoodsName.text = goods.name
        tvGoodsName.isSelected = true

        GlideUtils.loadToImageViewWithPlaceHolder(
            ivGoodsImage,
            goods.getImageRandom(),
            R.drawable.no_image
        )

        val unit = if (goods.medicineCategory == null) goods.unit else goods.medicineCategory?.unit

        tvAmount.text =
            "Số lượng: ${removeUnMean(goods.amount.toDouble())} ($unit)"
        tvLimit.text =
            "Giới hạn tồn: ${removeUnMean(goods.inventoryAtleast.toDouble())} - ${removeUnMean(goods.inventoryMost.toDouble())} ($unit)"
        tvLimit.isSelected = true

        tvGoodsPrice.text = "${convertToVietNamCurrentcy(goods.exportPrice)}đ"
        tvGoodsPrice.isSelected = true
    }
}