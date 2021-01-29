package com.example.quiz.activity

import android.os.Bundle
import com.example.quiz.R
import com.example.quiz.ShopHopAppBaseActivity
import com.example.quiz.adapter.ShopHopWalkAdapter
import com.example.quiz.utils.ShopHopCarouselEffectTransformer
import com.example.quiz.utils.ShopHopConstants.SharedPref.SHOW_SWIPE
import com.example.quiz.utils.extensions.*
import kotlinx.android.synthetic.main.shophop_activity_walk_through.*

class ShopHopWalkThroughActivity : ShopHopAppBaseActivity() {

    var mCount: Int? = null
    var mHeading = arrayOf("Hi, We're Woobox!", "Most Unique Styles!", "Shop Till You Drop!")
    private val mSubHeading = arrayOf(
        "We make around your city Affordable,\n easy and efficient.",
        "Shop the most trending fashion on the biggest shopping website.",
        "Grab the best seller pieces at bargain prices."
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shophop_activity_walk_through)
        init()
        val adapter = ShopHopWalkAdapter()

        ViewPager.adapter = adapter
        dots.attachViewPager(ViewPager)
        dots.setDotDrawable(R.drawable.shophop_bg_circle_primary, R.drawable.shophop_bg_black_dot)
        mCount = adapter.count
        if (getSharedPrefInstance().getBooleanValue(SHOW_SWIPE)) {
            launchActivity<ShopHopDashBoardActivity>()
            finish()
        }
        btnStatShopping.onClick {
            launchActivity<ShopHopDashBoardActivity>()
            getSharedPrefInstance().setValue(SHOW_SWIPE, true)
            finish()
        }
        llSignIn.onClick {
            launchActivity<ShopHopSignInUpActivity>()
        }
    }

    private fun init() {
        ViewPager.apply {
            clipChildren = false
            pageMargin = resources.getDimensionPixelOffset(R.dimen.sh_spacing_small)
            offscreenPageLimit = 3
            setPageTransformer(false,
                ShopHopCarouselEffectTransformer(this@ShopHopWalkThroughActivity)
            )
            offscreenPageLimit = 0

            onPageSelected { position: Int ->
                val animFadeIn = android.view.animation.AnimationUtils.loadAnimation(applicationContext, R.anim.shophop_fade_in)
                tvHeading.startAnimation(animFadeIn)
                tvSubHeading.startAnimation(animFadeIn)
                tvHeading.text = mHeading[position]
                tvSubHeading.text = mSubHeading[position]
            }
        }
    }
}