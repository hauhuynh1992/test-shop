package com.example.quiz.fragments

import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.quiz.ShopHopAppBaseActivity
import com.example.quiz.R
import com.example.quiz.utils.extensions.color


abstract class ShopHopBaseFragment : Fragment(),  View.OnFocusChangeListener {

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if (hasFocus) {
            (v as EditText).setTextColor(activity!!.color(R.color.ShopHop_colorPrimaryDark))
            v.background = activity!!.getDrawable(R.drawable.shophop_bg_ractangle_rounded_active)
        } else {
            (v as EditText).setTextColor(activity!!.color(R.color.ShopHop_textColorPrimary))
            v.background = activity!!.getDrawable(R.drawable.shophop_bg_ractangle_rounded_inactive)
        }
    }

    fun hideProgress() {
        if (activity != null)
            (activity!! as ShopHopAppBaseActivity).showProgress(false)
    }


    object BiggerDotTransformation : PasswordTransformationMethod() {

        override fun getTransformation(source: CharSequence, view: View): CharSequence {
            return PasswordCharSequence(super.getTransformation(source, view))
        }

        private class PasswordCharSequence(
            val transformation: CharSequence
        ) : CharSequence by transformation {
            override fun get(index: Int): Char = if (transformation[index] == DOT) {
                BIGGER_DOT
            } else {
                transformation[index]
            }
        }

        private const val DOT = '\u2022'
        private const val BIGGER_DOT = '●'
    }
}