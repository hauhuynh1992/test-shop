package com.example.quiz.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.quiz.R
import com.example.quiz.ShopHopAppBaseActivity
import com.example.quiz.fragments.ShopHopViewAllProductFragment
import com.example.quiz.utils.ShopHopConstants
import com.example.quiz.utils.extensions.addFragment
import com.example.quiz.utils.extensions.registerCartCountChangeReceiver
import kotlinx.android.synthetic.main.toolbar.*

class ShopHopViewAllProductActivity : ShopHopAppBaseActivity() {

    private var mFragment: ShopHopViewAllProductFragment? = null

    private val mCartCountChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (mFragment != null) {
                mFragment!!.setCartCount()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shophop_activity_view_all)
        setToolbar(toolbar)
        registerCartCountChangeReceiver(mCartCountChangeReceiver)

        title = intent.getStringExtra(ShopHopConstants.KeyIntent.TITLE)
        val mViewAllId = intent.getIntExtra(ShopHopConstants.KeyIntent.VIEWALLID, 0)
        val mCategoryId = intent.getStringExtra(ShopHopConstants.KeyIntent.KEYID)

        mFragment = ShopHopViewAllProductFragment.getNewInstance(mViewAllId, mCategoryId)
        addFragment(mFragment!!, R.id.fragmentContainer)
//        loadBannerAd(R.id.adView)

    }


    override fun onDestroy() {
        unregisterReceiver(mCartCountChangeReceiver)
        super.onDestroy()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}