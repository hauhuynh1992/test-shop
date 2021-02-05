package com.example.quiz.activity

import android.os.Bundle
import com.example.quiz.ShopHopAppBaseActivity
import com.example.quiz.R
import com.example.quiz.utils.extensions.launchActivity
import com.example.quiz.utils.extensions.runDelayed
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import com.example.quiz.ui.splash.SplashViewModel

class ShopHopSplashActivity : ShopHopAppBaseActivity() {

    lateinit var viewModel : SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shophop_activity_splash_new)
            val w = window
            w.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        runDelayed(500) {
            launchActivity<ShopHopWalkThroughActivity>(); onBackPressed()
        }
    }
}