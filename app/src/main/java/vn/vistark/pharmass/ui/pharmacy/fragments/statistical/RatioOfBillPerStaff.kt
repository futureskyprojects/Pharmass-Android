package vn.vistark.pharmass.ui.pharmacy.fragments.statistical

import android.R
import android.graphics.Color
import android.widget.Toast
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import vn.vistark.pharmass.core.model.Pharmacy
import vn.vistark.pharmass.processing.GetBillByPharmacyIdProcessing
import vn.vistark.pharmass.processing.GetUserByIdProcessing
import java.util.HashMap


class RatioOfBillPerStaff(val mChart: PieChart, val pharmacy: Pharmacy) :
    OnChartValueSelectedListener {
    init {
        mChart.isRotationEnabled = true
        mChart.description = Description()
        mChart.holeRadius = 35f
        mChart.setTransparentCircleAlpha(0)
        mChart.centerText = "Đơn bán/nhân viên"
        mChart.setCenterTextSize(10f)
        mChart.setDrawEntryLabels(true)

        addDataSet(mChart)

        mChart.setOnChartValueSelectedListener(this)
    }

    override fun onNothingSelected() {

    }

    override fun onValueSelected(e: Entry, h: Highlight) {
        Toast.makeText(
            mChart.context, "Value: "
                    + e.y
                    + ", index: "
                    + h.x
                    + ", DataSet index: "
                    + h.dataSetIndex, Toast.LENGTH_SHORT
        ).show()
    }

    private fun addDataSet(pieChart: PieChart) {
        GetBillByPharmacyIdProcessing(pieChart.context, pharmacy.id)
            .onFinished = { bills ->
            if (bills != null) {
                val mapData: HashMap<String, Int> = HashMap()
                for (bill in bills) {
                    mapData[bill.pharmacyStaff.user.toString()] =
                        (mapData[bill.pharmacyStaff.user.toString()] ?: 0) + 1
                }
                val entries: ArrayList<PieEntry> = ArrayList()
                val pieDataSet = PieDataSet(entries, "")
                for (map in mapData) {
                    GetUserByIdProcessing(pieChart.context, map.key.toInt())
                        .onFinished = { user ->
                        if (user != null && user.isNotEmpty()) {
                            entries.add(
                                PieEntry(
                                    map.value.toFloat(),
                                    user.first().fullName
                                )
                            )
                            pieDataSet.notifyDataSetChanged()
                            pieChart.invalidate()
                        }
                    }
                }

                pieDataSet.sliceSpace = 2f
                pieDataSet.valueTextSize = 12f
//                val colors: ArrayList<Int> = ArrayList()
//                colors.add(Color.GRAY)
//                colors.add(Color.BLUE)
//                colors.add(Color.RED)
//                pieDataSet.colors = colors
                val legend = pieChart.legend
                legend.isEnabled = true
                legend.form = Legend.LegendForm.CIRCLE

                legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
                legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
                legend.orientation = Legend.LegendOrientation.HORIZONTAL
                legend.setDrawInside(false)

                val pieData = PieData(pieDataSet)
                pieChart.data = pieData
                pieChart.invalidate()
            }
        }
    }
}