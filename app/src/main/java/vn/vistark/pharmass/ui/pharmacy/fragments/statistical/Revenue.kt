package vn.vistark.pharmass.ui.pharmacy.fragments.statistical

import android.graphics.Color
import android.widget.Toast
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import vn.vistark.pharmass.core.model.Bill
import vn.vistark.pharmass.processing.GetBillItemByIdProcessing
import vn.vistark.pharmass.utils.DateTimeUtils
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class Revenue(val mChart: CombinedChart, val bills: List<Bill>) : OnChartValueSelectedListener {
    init {
        val dataMap: HashMap<String, Float> = HashMap()
        for (bill in bills) {
            val date = DateTimeUtils.JsDateTimeStringToDate(bill.createdAt)
            if (date != null) {
                val dateKey = DateTimeUtils.DateToString(date, "dd/MM/yyyy")
                for (simpleBillItem in bill.simpleBillItems) {
                    GetBillItemByIdProcessing(mChart.context, simpleBillItem.id, true)
                        .onFinished = { billItem ->
                        if (billItem != null) {
                            dataMap[dateKey] =
                                dataMap[dateKey] ?: 0 + billItem.goods.exportPrice.toFloat()
                            println("Cập nhật doanh thu ngày $dateKey: ${dataMap[dateKey]}")
                        }
                    }
                }
            }
        }
        mChart.description.isEnabled = false
        mChart.setBackgroundColor(Color.WHITE)
        mChart.setDrawGridBackground(false)
        mChart.setDrawBarShadow(false)
        mChart.isHighlightFullBarEnabled = false
        mChart.setOnChartValueSelectedListener(this)

        val rightAxis = mChart.axisRight
        rightAxis.setDrawGridLines(false)
        rightAxis.axisMinimum = 0f

        val leftAxis = mChart.axisLeft
        leftAxis.setDrawGridLines(false)
        leftAxis.axisMinimum = 0f

        val xLabel: MutableList<String> = ArrayList()
        xLabel.add("Jan")
        xLabel.add("Feb")
        xLabel.add("Mar")
        xLabel.add("Apr")
        xLabel.add("May")
        xLabel.add("Jun")
        xLabel.add("Jul")
        xLabel.add("Aug")
        xLabel.add("Sep")
        xLabel.add("Oct")
        xLabel.add("Nov")
        xLabel.add("Dec")

        val xAxis = mChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.axisMinimum = 0f
        xAxis.granularity = 1f
        xAxis.valueFormatter =
            object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    val temp = value.toInt() % xLabel.size
//                    println(">>>>>>> Lấy tại vị trí $temp với giá trị là ${xLabel[temp]}")
                    return xLabel[value.toInt() % xLabel.size]
                }
            }

        val data = CombinedData()
        val lineDatas = LineData()
        lineDatas.addDataSet(dataChart() as ILineDataSet)

        data.setData(lineDatas)

        xAxis.axisMaximum = data.xMax + 0.25f



        mChart.data = data
        mChart.invalidate()
    }

    override fun onNothingSelected() {
    }

    override fun onValueSelected(e: Entry, h: Highlight) {
        Toast.makeText(
            mChart.context, "Value: "
                    + e.getY()
                    + ", index: "
                    + h.getX()
                    + ", DataSet index: "
                    + h.getDataSetIndex(), Toast.LENGTH_SHORT
        ).show();
    }

    private fun dataChart(): DataSet<*>? {
        val d = LineData()
        val data = intArrayOf(1, 2, 2, 1, 1, 1, 2, 1, 1, 2, 1, 9)
        val entries =
            ArrayList<Entry>()
        for (index in 0..11) {
            entries.add(Entry(index.toFloat(), data[index].toFloat()))
        }
        val set = LineDataSet(entries, "Request Ots approved")
        set.color = Color.GREEN
        set.lineWidth = 2.5f
        set.setCircleColor(Color.GREEN)
        set.circleRadius = 5f
        set.fillColor = Color.GREEN
        set.mode = LineDataSet.Mode.CUBIC_BEZIER
        set.setDrawValues(true)
        set.valueTextSize = 10f
        set.valueTextColor = Color.GREEN
        set.axisDependency = YAxis.AxisDependency.LEFT
        d.addDataSet(set)
        return set
    }
}