package com.example.quiz.activity

import android.os.Bundle
import com.example.quiz.ShopHopAppBaseActivity
import com.example.quiz.R
import android.widget.FrameLayout
import com.example.quiz.fragments.ShopHopSignInFragment
import com.example.quiz.fragments.ShopHopSignUpFragment
import com.example.quiz.utils.extensions.addFragment
import com.example.quiz.utils.extensions.fadeIn
import com.example.quiz.utils.extensions.removeFragment
import com.example.quiz.utils.extensions.replaceFragment


class ShopHopSignInUpActivity : ShopHopAppBaseActivity() {

    private val mSignInFragment: ShopHopSignInFragment =
        ShopHopSignInFragment()
    private val mSignUpFragment: ShopHopSignUpFragment =
        ShopHopSignUpFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shophop_activity_sign_in_up)

        /**
         * Load Default Fragment
         */
        loadSignInFragment()
    }

    fun loadSignUpFragment() {
        if (mSignUpFragment.isAdded) {
            replaceFragment(mSignUpFragment, R.id.fragmentContainer)
            findViewById<FrameLayout>(R.id.fragmentContainer).fadeIn(500)
        } else {
            addFragment(mSignUpFragment, R.id.fragmentContainer)
        }
    }

    fun loadSignInFragment() {
        if (mSignInFragment.isAdded) {
            replaceFragment(mSignInFragment, R.id.fragmentContainer)
            findViewById<FrameLayout>(R.id.fragmentContainer).fadeIn(500)
        } else {
            addFragment(mSignInFragment, R.id.fragmentContainer)
        }
    }

    override fun onBackPressed() {
        when {
            mSignUpFragment.isVisible -> {
                removeFragment(mSignUpFragment)
            }
            else -> super.onBackPressed()

        }
    }
}