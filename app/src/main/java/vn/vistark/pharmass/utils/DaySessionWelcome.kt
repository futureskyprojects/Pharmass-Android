package vn.vistark.pharmass.utils

import java.util.*

class DaySessionWelcome {
    companion object {
        fun getForNow(): String {
            val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

            if (hour in 2..10) {
                return "Chào buổi sáng"
            }
            if (hour in 11..14) {
                return "Chào buổi trưa"
            }
            if (hour in 15..17) {
                return "Chào buổi chiều"
            }
            return "Chào buổi tối"
        }
    }
}