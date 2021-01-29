package com.example.quiz.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.quiz.ShopHopAppBaseActivity
import com.example.quiz.R
import com.example.quiz.fragments.ShopHopMyCartFragment
import com.example.quiz.utils.ShopHopConstants
import com.example.quiz.utils.extensions.addFragment
import com.example.quiz.utils.extensions.registerCartReceiver
import kotlinx.android.synthetic.main.toolbar.*

class ShopHopMyCartActivity : ShopHopAppBaseActivity() {
    private val mCartItemChangedReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                ShopHopConstants.AppBroadcasts.CARTITEM_UPDATE -> {
                    myCartFragment.setCart()
                }
            }
        }
    }
    private var myCartFragment:ShopHopMyCartFragment=ShopHopMyCartFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shophop_activity_my_cart)
        setToolbar(toolbar)
        title = getString(R.string.sh_menu_my_cart)
        registerCartReceiver(mCartItemChangedReceiver)
        addFragment(myCartFragment, R.id.container)

    }
    override fun onDestroy() {
        unregisterReceiver(mCartItemChangedReceiver)
        super.onDestroy()
    }
    override fun onBackPressed() {
        super.onBackPressed()
    }

}
