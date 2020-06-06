package vn.vistark.pharmass.utils

class UrlUtils {
    companion object {
        fun standard(url: String): String {
            val protocol: String
            if (url.indexOf("https://") == 0) {
                protocol = "https://"
            } else if (url.indexOf("http://") == 0) {
                protocol = "http://"
            } else return url
            return protocol + url.replace("(https{0,1})://".toRegex(), "").replace("//", "/")
                .replace("//", "/")
        }
    }
}