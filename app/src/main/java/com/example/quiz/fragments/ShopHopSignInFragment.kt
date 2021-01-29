package com.example.quiz.fragments

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RelativeLayout
import com.example.quiz.R
import com.example.quiz.activity.ShopHopDashBoardActivity
import com.example.quiz.activity.ShopHopSignInUpActivity
import com.example.quiz.utils.ShopHopOTPEditText
import com.example.quiz.utils.extensions.*
import kotlinx.android.synthetic.main.shophop_dialog_change_password.*
import kotlinx.android.synthetic.main.shophop_fragment_sign_in.*

class ShopHopSignInFragment : ShopHopBaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.shophop_fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeFragment()
    }

    private fun initializeFragment() {
        edtEmail.onFocusChangeListener = this
        edtPassword.onFocusChangeListener = this
        edtPassword.transformationMethod = BiggerDotTransformation

        edtEmail.setSelection(edtEmail.length())
        btnSignIn.onClick {
            if (validate()) {
                doLogin()
            }
        }

        tvForget.onClick {
            snackBar("Coming Soon")
        }

        btnSignUp.onClick {
            (activity as ShopHopSignInUpActivity).loadSignUpFragment()
        }
        tvForget.onClick {
            showChangePasswordDialog()
        }
    }

    private fun validate(): Boolean {
        return when {
            edtEmail.checkIsEmpty() -> {
                edtEmail.showError(getString(R.string.sh_error_field_required))
                false
            }
            !edtEmail.isValidEmail() -> {
                edtEmail.showError(getString(R.string.sh_error_enter_valid_email))
                false
            }
            edtPassword.checkIsEmpty() -> {
                edtPassword.showError(getString(R.string.sh_error_field_required))
                false
            }
            else -> true
        }
    }

    private fun doLogin() {
        signIn(onResult = {
            if (it) {
                if (activity != null) {
                    activity!!.launchActivityWithNewTask<ShopHopDashBoardActivity>()
                }
            }
        })
    }
    private var mEds: Array<EditText?>? = null

    private fun showChangePasswordDialog() {
        //if (changePasswordDialog==null){
        val changePasswordDialog = Dialog(activity!!)
        changePasswordDialog.window?.setBackgroundDrawable(ColorDrawable(0))
        changePasswordDialog.setContentView(R.layout.shophop_dialog_change_password)
        changePasswordDialog.window?.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        changePasswordDialog.edtNewPassword.transformationMethod = BiggerDotTransformation
        changePasswordDialog.edtconfirmPassword.transformationMethod = BiggerDotTransformation
        mEds = arrayOf(changePasswordDialog.findViewById(R.id.edDigit1), changePasswordDialog.findViewById(R.id.edDigit2), changePasswordDialog.findViewById(R.id.edDigit3), changePasswordDialog.findViewById(R.id.edDigit4))
        ShopHopOTPEditText(mEds!!, activity!!, activity?.getDrawable(R.color.ShopHop_transparent)!!, activity?.getDrawable(R.drawable.shophop_bg_unselected_dot)!!)
        mEds!!.forEachIndexed { _, editText ->
            editText?.onFocusChangeListener = focusChangeListener
        }
        changePasswordDialog.show()
    }
    private val focusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
        if (hasFocus)
            (v as EditText).background = activity?.getDrawable(R.color.ShopHop_transparent)
    }


}