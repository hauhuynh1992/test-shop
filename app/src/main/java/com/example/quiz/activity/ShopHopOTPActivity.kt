package com.example.quiz.activity

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.EditText
import com.example.quiz.ShopHopAppBaseActivity
import com.example.quiz.R
import com.example.quiz.utils.ShopHopConstants
import com.example.quiz.utils.ShopHopOTPEditText
import com.example.quiz.utils.extensions.*
import kotlinx.android.synthetic.main.shophop_activity_otp.*
import kotlinx.android.synthetic.main.shophop_layout_otp.*
import java.util.*

class ShopHopOTPActivity : ShopHopAppBaseActivity() {

    private var mEds: Array<EditText?>? = null
    private var timer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shophop_activity_otp)
        mEds = arrayOf(edDigit1, edDigit2, edDigit3, edDigit4)
        ShopHopOTPEditText(
            mEds!!,
            this,
            getDrawable(R.color.ShopHop_transparent)!!,
            getDrawable(R.drawable.shophop_bg_unselected_dot)!!
        )
        mEds!!.forEachIndexed { _, editText ->
            editText?.onFocusChangeListener = focusChangeListener
        }
        timer = startOTPTimer(onTimerTick = {
            tvTimer.text = it
        }, onTimerFinished = {
            tvTimer.hide()
            llResend.show()
        })
        timer?.start()
        tvResend.onClick {
            tvTimer.show()
            llResend.hide()
            timer?.start()

        }
        ivBack.onClick {
            onBackPressed()
        }
        btnVerify.onClick {
            getSharedPrefInstance().setValue(ShopHopConstants.SharedPref.IS_LOGGED_IN, true)
            getSharedPrefInstance().setValue(ShopHopConstants.SharedPref.USER_ID, Random().nextInt(10000).toString())
            launchActivityWithNewTask<ShopHopDashBoardActivity>()
        }
    }

    private val focusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
        if (hasFocus)
            (v as EditText).background = getDrawable(R.color.ShopHop_transparent)
    }


}