package vn.vistark.pharmass.utils

import android.graphics.Color
import java.util.*

class ColorUtils {
    companion object {
        fun random(): Int {
            val rnd = Random()
            return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
        }
    }
}