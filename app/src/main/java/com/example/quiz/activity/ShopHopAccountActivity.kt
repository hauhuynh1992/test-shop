package com.example.quiz.activity

import android.app.Activity
import android.os.Bundle
import com.example.quiz.ShopHopAppBaseActivity
import com.example.quiz.R
import com.example.quiz.utils.extensions.*
import kotlinx.android.synthetic.main.shophop_activity_account.*
import kotlinx.android.synthetic.main.toolbar.*

class ShopHopAccountActivity : ShopHopAppBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shophop_activity_account)
        setToolbar(toolbar)
        title = getString(R.string.sh_title_account)

        txtDisplayName.text = getUserFullName()

        btnSignOut.onClick {
            val dialog = getAlertDialog(getString(R.string.sh_lbl_logout_confirmation), onPositiveClick = { _, _ ->
                clearLoginPref()
                launchActivityWithNewTask<ShopHopDashBoardActivity> ()
            }, onNegativeClick = { dialog, _ ->
                dialog.dismiss()
            })
            dialog.show()
        }
        tvOrders.onClick {
            launchActivity<ShopHopOrderActivity>()
        }
        tvQuickPay.onClick {
            launchActivity<ShopHopQuickPayActivity>()
        }
        tvOffer.onClick {
            launchActivity<ShopHopOfferActivity>()
        }
        btnVerify.onClick {
            launchActivity<ShopHopOTPActivity>()
        }
        tvAddressManager.onClick {
            if (getAddressList().size == 0) {
                launchActivity<ShopHopAddAddressActivity>()
            } else {
                launchActivity<ShopHopAddressManagerActivity>()
            }
        }
        ivProfileImage.onClick {
            launchActivity<ShopHopEditProfileActivity> ()
        }
        tvWishlist.onClick {
            setResult(Activity.RESULT_OK)
            finish()
        }
        tvHelpCenter.onClick {
            launchActivity<ShopHopHelpActivity>()
        }
//        loadBannerAd(R.id.adView)
    }

    override fun onBackPressed() {
        super.onBackPressed()

    }
}