package vn.vistark.pharmass.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateTimeUtils {
    companion object {
        fun JsDateTimeStringToDate(string: String): Date? {
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss'Z'")
            try {
                return format.parse(string)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return null
        }

        fun DateToJsDateTimeString(date: Date): String {
            val dateFormat =
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
            try {
                return dateFormat.format(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return ""
        }

        fun DateToString(date: Date, format: String): String {
            val dateFormat =
                SimpleDateFormat(format)
            try {
                return dateFormat.format(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return ""
        }

        fun StringToDate(string: String, format: String): Date? {
            val dateFormat =
                SimpleDateFormat(format)
            try {
                return dateFormat.parse(string)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return null
        }

    }
}