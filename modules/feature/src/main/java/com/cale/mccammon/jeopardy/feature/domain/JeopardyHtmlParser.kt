package com.cale.mccammon.jeopardy.feature.domain

import android.text.Html
import javax.inject.Inject

interface JeopardyHtmlParser {
    fun fromHtml(text: String): String
}

class JeopardyHtmlParserImpl @Inject constructor(): JeopardyHtmlParser {
    override fun fromHtml(text: String): String {
        return Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
            .toString()
            .replace("\n", "")
            .trim()
    }

}
