package com.example.quiz.utils.extensions

import com.example.quiz.utils.ShopHopConstants

fun toDate(string: String, currentFormat: Int = ShopHopConstants.DateFormatCodes.YMD_HMS): String {
    return when (currentFormat) {
        ShopHopConstants.DateFormatCodes.YMD_HMS -> ShopHopConstants.DD_MMM_YYYY.format(ShopHopConstants.FULL_DATE_FORMATTER.parse(string)!!)
        ShopHopConstants.DateFormatCodes.YMD -> ShopHopConstants.DD_MMM_YYYY.format(ShopHopConstants.YYYY_MM_DD.parse(string)!!)
        else -> string
    }
}