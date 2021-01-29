package com.example.quiz.activity

import android.os.Bundle
import com.example.quiz.ShopHopAppBaseActivity
import com.example.quiz.R
import com.example.quiz.fragments.ShopHopSearchFragment
import com.example.quiz.utils.extensions.addFragment

class ShopHopSearchActivity : ShopHopAppBaseActivity() {

    private val mSearchFragment = ShopHopSearchFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shophop_activity_search2)
        addFragment(mSearchFragment, R.id.fragmentContainer)
//        loadBannerAd(R.id.adView)
    }

    override fun onBackPressed() {
        super.onBackPressed()


    }
}