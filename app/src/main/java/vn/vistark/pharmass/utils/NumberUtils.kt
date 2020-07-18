package vn.vistark.pharmass.utils

import java.text.DecimalFormat
import java.text.NumberFormat

class NumberUtils {
    companion object {
        fun removeUnMean(number: Double): String {
            if ("$number".contains("(.*?)\\.0$".toRegex())) {
                return "$number".replace(".0", "")
            }
            return "$number"
        }

        fun convertToVietNamCurrentcy(number: Double): String {
            val formatter: NumberFormat = DecimalFormat("#,###")
            return formatter.format(number)
        }
    }
}