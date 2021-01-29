package com.example.quiz.activity

import android.os.Bundle
import com.example.quiz.ShopHopAppBaseActivity
import com.example.quiz.R
import com.example.quiz.fragments.ShopHopProfileFragment
import com.example.quiz.utils.extensions.addFragment
import kotlinx.android.synthetic.main.toolbar.*

class ShopHopEditProfileActivity : ShopHopAppBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shophop_activity_edit_profile)
        setToolbar(toolbar)
        title=getString(R.string.sh_lbl_edit_profile)
        addFragment(ShopHopProfileFragment(),R.id.profilecontainer)
//        loadBannerAd(R.id.adView)

    }
}
