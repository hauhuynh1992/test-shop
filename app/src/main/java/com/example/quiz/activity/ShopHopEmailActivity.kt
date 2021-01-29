package com.example.quiz.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.quiz.R
import com.example.quiz.ShopHopAppBaseActivity
import com.example.quiz.utils.extensions.*
import kotlinx.android.synthetic.main.shophop_activity_email.*
import kotlinx.android.synthetic.main.shophop_menu_cart.view.*
import kotlinx.android.synthetic.main.toolbar.*


class ShopHopEmailActivity : ShopHopAppBaseActivity() {
    private lateinit var mMenuCart: View
    private val mCartCountChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            setCartCount()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shophop_activity_email)
        title = getString(R.string.sh_lbl_email)
        setToolbar(toolbar)
        registerCartCountChangeReceiver(mCartCountChangeReceiver)
        btnSendMail.onClick {
            when {
                validate() -> {
                    val emailIntent = Intent(
                        Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto", getString(R.string.sh_text_iqonicdesign_gmail_com), null
                        )
                    )
                    emailIntent.putExtra(Intent.EXTRA_TEXT,edtDescription.text.toString() )
                    startActivity(Intent.createChooser(emailIntent, "Send email..."))
                }
            }
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
        setCartCount()
        menuWishItem.actionView.onClick {
            launchActivity<ShopHopMyCartActivity> { }
        }
        return super.onCreateOptionsMenu(menu)
    }

    fun setCartCount() {
        val count = getCartCount()
        mMenuCart.tvNotificationCount.text = count
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

    private fun validate(): Boolean {
        return when {
           /* edtEmail.checkIsEmpty() -> {
                edtEmail.showError(getString(R.string.error_field_required))
                false
            }
            !edtEmail.isValidEmail() -> {
                edtEmail.showError(getString(R.string.error_enter_valid_email))
                false
            }*/
            edtDescription.checkIsEmpty() -> {
                edtDescription.showError(getString(R.string.sh_error_field_required))
                false
            }
            else -> true
        }
    }

}
