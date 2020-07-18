package vn.vistark.pharmass.ui.pharmacy_bill

import android.media.Image
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.ybq.android.spinkit.SpinKitView
import vn.vistark.pharmass.R
import vn.vistark.pharmass.core.model.BillItem
import vn.vistark.pharmass.core.model.Goods
import vn.vistark.pharmass.processing.GetGoodsByIdProcessing
import vn.vistark.pharmass.utils.GlideUtils
import vn.vistark.pharmass.utils.NumberUtils.Companion.convertToVietNamCurrentcy
import vn.vistark.pharmass.utils.NumberUtils.Companion.removeUnMean

class BillItemViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    val rlRoot: RelativeLayout = v.findViewById(R.id.rlRoot)
    val tvGoodsName: TextView = v.findViewById(R.id.tvGoodsName)
    val tvAmount: TextView = v.findViewById(R.id.tvAmount)
    val tvLimit: TextView = v.findViewById(R.id.tvLimit)
    val ivGoodsImage: ImageView = v.findViewById(R.id.ivGoodsImage)
    val tvGoodsPrice: TextView = v.findViewById(R.id.tvGoodsPrice)

    val ivS2Icon: ImageView = v.findViewById(R.id.ivS2Icon)

    val loadindForGoodsItem: SpinKitView = v.findViewById(R.id.loadindForGoodsItem)
    val rlGoodsItemVisible: RelativeLayout = v.findViewById(R.id.rlGoodsItemVisible)

    fun bind(billItem: BillItem) {
        loadindForGoodsItem.visibility = View.VISIBLE
        rlGoodsItemVisible.visibility = View.GONE
        GetGoodsByIdProcessing(rlRoot.context, billItem.goods).onFinished = { goods ->
            if (goods != null) {
                loadindForGoodsItem.visibility = View.GONE
                rlGoodsItemVisible.visibility = View.VISIBLE
                ivS2Icon.setImageResource(R.drawable.ic_smartphone_direction)
                tvGoodsName.text = goods.name
                tvGoodsName.isSelected = true

                GlideUtils.loadToImageViewWithPlaceHolder(
                    ivGoodsImage,
                    goods.getImageRandom(),
                    R.drawable.no_image
                )

                val unit =
                    if (goods.medicineCategory == null) goods.unit else goods.medicineCategory?.unit

                tvAmount.text =
                    "Số lượng: ${removeUnMean(billItem.dosage.toDouble())} ($unit)"
                tvLimit.text =
                    billItem.direction
                tvLimit.isSelected = true

                tvGoodsPrice.text = "${convertToVietNamCurrentcy(goods.exportPrice)}đ"
                tvGoodsPrice.isSelected = true
            } else {
                bind(billItem)
            }
        }
    }
}