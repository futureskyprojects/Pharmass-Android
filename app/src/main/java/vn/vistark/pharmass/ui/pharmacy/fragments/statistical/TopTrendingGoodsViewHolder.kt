package vn.vistark.pharmass.ui.pharmacy.fragments.statistical

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

class TopTrendingGoodsViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    val rlRoot: RelativeLayout = v.findViewById(R.id.rlRoot)
    val tvGoodsName: TextView = v.findViewById(R.id.tvGoodsName)
    val tvAmount: TextView = v.findViewById(R.id.tvAmount)
    val tvLimit: TextView = v.findViewById(R.id.tvLimit)
    val ivGoodsImage: ImageView = v.findViewById(R.id.ivGoodsImage)
    val tvGoodsPrice: TextView = v.findViewById(R.id.tvGoodsPrice)
    val ivS2Icon: ImageView = v.findViewById(R.id.ivS2Icon)

    fun bind(topTrendingGoodsInfo: TopTrendingGoodsAdapter.Companion.TopTrendingGoodsInfo) {
        tvGoodsName.text = topTrendingGoodsInfo.goods.name
        tvGoodsName.isSelected = true

        GlideUtils.loadToImageViewWithPlaceHolder(
            ivGoodsImage,
            topTrendingGoodsInfo.goods.getImageRandom(),
            R.drawable.no_image
        )

        val unit =
            if (topTrendingGoodsInfo.goods.medicineCategory == null) topTrendingGoodsInfo.goods.unit else topTrendingGoodsInfo.goods.medicineCategory?.unit

        tvAmount.text =
            "Bán ra: ${removeUnMean(topTrendingGoodsInfo.dosage)} ($unit)"

        ivS2Icon.setImageResource(R.drawable.ic_dollar)
        tvLimit.text =
            "Doanh thu: ${convertToVietNamCurrentcy(topTrendingGoodsInfo.totalMoney)} (VNĐ)"
        tvLimit.isSelected = true

        tvGoodsPrice.text = "${convertToVietNamCurrentcy(topTrendingGoodsInfo.goods.exportPrice)}đ"
        tvGoodsPrice.isSelected = true
    }
}