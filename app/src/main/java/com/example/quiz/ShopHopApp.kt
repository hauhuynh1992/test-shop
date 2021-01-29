package com.example.quiz

import android.app.Application
import android.app.Dialog
import android.content.Context
import androidx.multidex.MultiDex
import com.example.quiz.utils.ShopHopSharedPrefUtils
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump

open class ShopHopApp : Application() {

    override fun onCreate() {
        super.onCreate()
        appInstance = this

        // Set Custom Font
        ViewPump.init(ViewPump.builder().addInterceptor(CalligraphyInterceptor(
                CalligraphyConfig.Builder().setDefaultFontPath(getString(R.string.sh_font_regular)).setFontAttrId(R.attr.fontPath).build())
        ).build())
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    companion object {
        private lateinit var appInstance: ShopHopApp
        var sharedPrefUtils: ShopHopSharedPrefUtils? = null
        var noInternetDialog : Dialog?= null

        fun getAppInstance(): ShopHopApp {
            return appInstance
        }
    }
}
