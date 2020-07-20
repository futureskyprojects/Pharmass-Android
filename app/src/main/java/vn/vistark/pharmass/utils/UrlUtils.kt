package vn.vistark.pharmass.utils

import vn.vistark.pharmass.core.api.RetrofitClient

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

        fun truePathOfMyServer(path: String?): String {
            if (path == null)
                return ""
            if (path.isEmpty())
                return ""
            if (path.contains(RegexLibs.url.toRegex()))
                return path
            else
                return standard("${RetrofitClient.BASE_URL}/$path")
        }
    }
}