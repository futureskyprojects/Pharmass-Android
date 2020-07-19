package vn.vistark.pharmass.ui.pharmacy.fragments.statistical

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.ybq.android.spinkit.SpinKitView
import vn.vistark.pharmass.core.model.Bill
import vn.vistark.pharmass.core.model.BillItem
import vn.vistark.pharmass.processing.GetGoodsByIdProcessing
import java.util.stream.Collectors

class TopTrendingGoods(
    val rvTopTrendingGoods: RecyclerView,
    val skvLoadingIcon: SpinKitView,
    val bills: List<Bill>
) {
    var topTrendingGoodsInfos = ArrayList<TopTrendingGoodsAdapter.Companion.TopTrendingGoodsInfo>()
    var adapter: TopTrendingGoodsAdapter

    init {
        rvTopTrendingGoods.setHasFixedSize(true)
        rvTopTrendingGoods.layoutManager = LinearLayoutManager(rvTopTrendingGoods.context)

        adapter = TopTrendingGoodsAdapter(topTrendingGoodsInfos)
        rvTopTrendingGoods.adapter = adapter

        skvLoadingIcon.visibility = View.VISIBLE

        loadData()
    }

    private fun loadData() {
        var totalLoad = countFromBill()
        for (bill in bills) {
            for (billItem in bill.simpleBillItems) {
                GetGoodsByIdProcessing(rvTopTrendingGoods.context, billItem.goods)
                    .onFinished = { goods ->
                    if (goods != null) {
                        val topTrendingGoodsInfo =
                            TopTrendingGoodsAdapter.Companion.TopTrendingGoodsInfo(
                                goods,
                                billItem.dosage.toDouble(),
                                billItem.dosage.toDouble() * goods.exportPrice
                            )
                        // Kiem Tra xem san pham da co trong danh sach chua
                        var isDupplicated = false
                        for (i in topTrendingGoodsInfos.indices) {
                            if (topTrendingGoodsInfos[i].goods.id == topTrendingGoodsInfo.goods.id) {
                                topTrendingGoodsInfos[i].totalMoney += topTrendingGoodsInfo.totalMoney
                                topTrendingGoodsInfos[i].dosage += topTrendingGoodsInfo.dosage
                                isDupplicated = true
                            }
                        }
                        // Tien hanh them vao va xu ly
                        if (!isDupplicated)
                            topTrendingGoodsInfos.add(topTrendingGoodsInfo)
                        totalLoad--
                        if (totalLoad <= 0) {
                            val temp = topTrendingGoodsInfos.sortedByDescending { it.totalMoney }
                            topTrendingGoodsInfos.clear()
                            for (i in temp.indices) {
                                if (i <= 9) {
                                    topTrendingGoodsInfos.add(temp[i])
                                }
                            }

                            adapter.notifyDataSetChanged()
                            skvLoadingIcon.visibility = View.GONE
                            rvTopTrendingGoods.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

    private fun countFromBill(): Int {
        var tempCount = 0
        for (bill in bills) {
            tempCount += bill.simpleBillItems.size
        }
        return tempCount
    }
}