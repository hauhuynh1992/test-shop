package com.example.quiz.utils.extensions

import android.os.Build
import android.text.Html
import android.text.Spanned
import android.util.Base64
import androidx.core.text.HtmlCompat
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.security.MessageDigest
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

fun String.checkIsEmpty(): Boolean =
    isNullOrEmpty() || "" == this || this.equals("null", ignoreCase = true)

fun String.getHtmlString(): Spanned = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
    Html.fromHtml(this, HtmlCompat.FROM_HTML_MODE_LEGACY)
} else {
    Html.fromHtml(this)
}

fun String.toCamelCase(): String {
    var stringBuilder = StringBuilder()
    try {
        val toLowerCase = this.toLowerCase(Locale.getDefault())
        if (toLowerCase.isNotEmpty()) {
            for (toProperCase in toLowerCase.trim { it <= ' ' }.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
                stringBuilder.append(" ").append(toProperCase(toProperCase))
            }
        }
    } catch (e: NullPointerException) {
        stringBuilder = StringBuilder()
    }

    return stringBuilder.toString()
}

fun toProperCase(str: String): String {
    return try {
        if (str.isNotEmpty()) str.substring(
            0,
            1
        ).toUpperCase(Locale.getDefault()) + str.substring(1).toLowerCase(Locale.getDefault()) else ""
    } catch (e: NullPointerException) {
        ""
    }
}


fun String.currencyFormat(code: String = "USD"): String {
    if (this.checkIsEmpty()) {
        return this
    }
    return when (code) {
        "USD" -> "$$this"
//        "INR" -> "₹$this"
        else -> "₹$this"
    }

}
fun String.isValidColor(): Boolean {
    return (contains("#") && length >= 6)
}
