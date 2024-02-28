package com.beweaver.spacexlaunches.utils

import java.util.regex.Matcher
import java.util.regex.Pattern

class StringUtil {
    companion object {
        fun getVideoId(videoUrl: String?): String? {
            val reg =
                "(?:youtube(?:-nocookie)?\\.com\\/(?:[^\\/\\n\\s]+\\/\\S+\\/|(?:v|e(?:mbed)?)\\/|\\S*?[?&]v=)|youtu\\.be\\/)([a-zA-Z0-9_-]{11})"
            val pattern: Pattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE)
            val matcher: Matcher = pattern.matcher(videoUrl)
            return if (matcher.find()) matcher.group(1) else null
        }
    }
}