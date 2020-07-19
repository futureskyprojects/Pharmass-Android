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
import java.lang.Exception
import java.util.Comparator


class Revenue(val mChart: CombinedChart, val bills: List<Bill>) : OnChartValueSelectedListener {
    init {
        try {
            mChart.invalidate()
            mChart.clear()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        mChart.description.isEnabled = false
        mChart.setBackgroundColor(Color.WHITE)
        mChart.setDrawGridBackground(false)
        mChart.setDrawBarShadow(false)
        mChart.isHighlightFullBarEnabled = false
        mChart.setOnChartValueSelectedListener(this)

        mChart.setNoDataTextColor(Color.WHITE)

        val rightAxis = mChart.axisRight
        rightAxis.setDrawGridLines(false)
        rightAxis.axisMinimum = 0f

        val leftAxis = mChart.axisLeft
        leftAxis.setDrawGridLines(false)
        leftAxis.axisMinimum = 0f

        processingData()
    }

    private fun getLabel(dataMap: java.util.HashMap<String, Float>): ArrayList<String> {
        val temp = ArrayList<String>()
        dataMap.forEach {
            temp.add(it.key)
            println("Khóa: ${it.key} >>>>>>>>>> Đã được thêm vào DS")
        }
        return temp
    }

    private fun getValue(dataMap: java.util.HashMap<String, Float>): Array<Float> {
        var temp: Array<Float> = emptyArray()
        dataMap.forEach {
            temp = temp.plus(it.value)
            println("Giá trị: ${it.value} >>>>>>>>>> Đã được thêm vào DS")
        }
        return temp
    }

    private fun processingData() {
        var sumLoadRoute = 0

        // Dem tong so lan lap bat dong bo de doi
        for (bill in bills) {
            for (simpleBillItem in bill.simpleBillItems) {
                sumLoadRoute++
            }
        }

        var dataMap: HashMap<String, Float> = HashMap()
        for (bill in bills) {
            val date = DateTimeUtils.JsDateTimeStringToDate(bill.createdAt)
            if (date != null) {
                val dateKey = DateTimeUtils.DateToString(date, "dd/MM/yyyy")
                for (simpleBillItem in bill.simpleBillItems) {
                    GetBillItemByIdProcessing(mChart.context, simpleBillItem.id, true)
                        .onFinished = { billItem ->
                        if (billItem != null) {
                            println("Update for ${dateKey} >>>> ${dataMap[dateKey]}")
                            dataMap[dateKey] =
                                dataMap[dateKey] ?: 0 + billItem.goods.exportPrice.toFloat()
                            println("End update for ${dateKey} >>>> ${dataMap[dateKey]}")
                            sumLoadRoute--
                            if (sumLoadRoute <= 0) {
                                println("SS1: Đã hoàn tất với ${dataMap.size} phần tử")
                                // Sap sep lai

                                // ====== XU LY TIEP THEO ====== //
                                val dataCollectionMap = processingDataCollection(dataMap)

                                val xLabel: MutableList<String> = getLabel(dataCollectionMap)

                                val xAxis = mChart.xAxis
                                xAxis.position = XAxis.XAxisPosition.BOTTOM
                                xAxis.axisMinimum = 0f
                                xAxis.granularity = 1f
                                xAxis.valueFormatter =
                                    object : ValueFormatter() {
                                        override fun getFormattedValue(value: Float): String {
                                            return xLabel[value.toInt() % xLabel.size]
                                        }
                                    }

                                val data = CombinedData()
                                val lineDatas = LineData()
                                lineDatas.addDataSet(dataChart(dataCollectionMap) as ILineDataSet)

                                data.setData(lineDatas)

                                xAxis.axisMaximum = data.xMax + 0.25f



                                mChart.data = data
                                mChart.invalidate()
                                // ====== END END END END ====== //
                            }
                        }
                    }
                }
            }
        }
    }

    private fun processingDataCollection(temp: HashMap<String, Float>): HashMap<String, Float> {
        var dataMap = HashMap<String, Float>()

        var isSameYear = true
        // Kiem tra xem co cung nam hay khong
        var firstGotYear = ""
        for (dm in temp) {
            val tempDate = DateTimeUtils.StringToDate(dm.key, "dd/MM/yyyy")
            if (tempDate != null) {
                val year = DateTimeUtils.DateToString(tempDate, "yyyy")
                if (firstGotYear.isEmpty()) {
                    firstGotYear = year
                } else if (firstGotYear != year) {
                    isSameYear = false
                    break
                }
            }
        }

        if (isSameYear) {
            println("================= SAME YEAR")
        } else {
            println("================= NOT SAME YEAR")
        }

        if (temp.size < 31 * 2) {
            // Thong ke theo ngay
            if (isSameYear) {
                for (dm in temp) {
                    val tempDate = DateTimeUtils.StringToDate(dm.key, "dd/MM/yyyy")
                    if (tempDate != null) {
                        val newKey = DateTimeUtils.DateToString(tempDate, "dd/MM")
                        dataMap[newKey] = dataMap[newKey] ?: 0 + dm.value
                    }
                }
            } else {
                // copy sang het
                dataMap = temp
            }
        } else {
            for (dm in temp) {
                val tempDate = DateTimeUtils.StringToDate(dm.key, "dd/MM/yyyy")
                if (tempDate != null) {
                    val newKey = DateTimeUtils.DateToString(tempDate, "MM/yyyy")
                    dataMap[newKey] = dataMap[newKey] ?: 0 + dm.value
                }
            }
        }
        println("SS2: Đã hoàn tất với ${dataMap.size} phần tử")
        return dataMap
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

    private fun dataChart(dataMap: HashMap<String, Float>): DataSet<*>? {
        val d = LineData()
        val data = getValue(dataMap)
        val entries =
            ArrayList<Entry>()

        for (i in data.indices) {
            entries.add(Entry(i.toFloat(), data[i]))
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