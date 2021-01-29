package com.example.quiz.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.quiz.ShopHopAppBaseActivity
import com.example.quiz.R
import com.example.quiz.adapter.ShopHopLanguageAdapter
import com.example.quiz.utils.extensions.*
import kotlinx.android.synthetic.main.shophop_menu_cart.view.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.shophop_activity_setting.*


class ShopHopSettingActivity : ShopHopAppBaseActivity() {
    private lateinit var mMenuCart: View

    private val mCartCountChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            setCartCount()
        }

    }
    private var mLanguageName = arrayOf("English (US)", "English (Canada)")
    private var mCountryImg = intArrayOf(R.drawable.shophop_ic_usflag, R.drawable.shophop_ic_canadaflag)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shophop_activity_setting)

        title = getString(R.string.sh_title_setting)
        setToolbar(toolbar)
        registerCartCountChangeReceiver(mCartCountChangeReceiver)
        val adapter = ShopHopLanguageAdapter(this, mCountryImg, mLanguageName)
        sLanguage.adapter = adapter

        if (isLoggedIn()) {
            tvEmail.text = getEmail()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.shophop_menu_dashboard, menu)
        val menuWishItem: MenuItem = menu!!.findItem(R.id.action_cart)
        val menuSearch: MenuItem = menu.findItem(R.id.action_search)
        menuWishItem.isVisible = true
        menuSearch.isVisible = false
        mMenuCart = menuWishItem.actionView
        mMenuCart.ivCart.setColorFilter(this.color(R.color.ShopHop_textColorPrimary))
        menuWishItem.actionView.onClick {
            launchActivity<ShopHopMyCartActivity> { }
        }
        setCartCount()
        return super.onCreateOptionsMenu(menu)
    }

    fun setCartCount() {
        val count = getCartCount()
        mMenuCart.tvNotificationCount.text =count
        if (count.checkIsEmpty()|| count=="0") {
            mMenuCart.tvNotificationCount.hide()
        } else {
            mMenuCart.tvNotificationCount.show()
        }
    }

    override fun onDestroy() {
        unregisterReceiver(mCartCountChangeReceiver)
        super.onDestroy()
    }
}


