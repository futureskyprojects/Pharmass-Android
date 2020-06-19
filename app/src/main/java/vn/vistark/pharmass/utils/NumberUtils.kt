package vn.vistark.pharmass.utils

class NumberUtils {
    companion object {
        fun removeUnMean(number: Double): String {
            if ("$number".contains("(.*?)\\.0$".toRegex())) {
                return "$number".replace(".0", "")
            }
            return "$number"
        }
    }
}