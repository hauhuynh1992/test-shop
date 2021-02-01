package com.example.quiz.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.quiz.ShopHopAppBaseActivity
import com.example.quiz.R
import com.example.quiz.activity.ShopHopSignInUpActivity
import com.example.quiz.models.ShopHopRequestModel
import com.example.quiz.utils.extensions.*
import kotlinx.android.synthetic.main.shophop_fragment_sign_up.*

class ShopHopSignUpFragment : ShopHopBaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.shophop_fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeFragment()
    }

    private fun initializeFragment() {
        edtEmail.onFocusChangeListener = this
        edtPassword.onFocusChangeListener = this
        edtConfirmPassword.onFocusChangeListener = this
        edtFirstName.onFocusChangeListener = this
        edtLastName.onFocusChangeListener = this
        edtPassword.transformationMethod =
            BiggerDotTransformation
        edtConfirmPassword.transformationMethod =
            BiggerDotTransformation

        btnSignUp.onClick {
            when {
                validate() -> {
                    createCustomer()
                }
            }
        }
        btnSignIn.onClick {
            (activity as ShopHopSignInUpActivity).loadSignInFragment()
        }
    }

    private fun createCustomer() {
        val requestModel = ShopHopRequestModel()
        requestModel.userEmail = edtEmail.textToString()
        requestModel.firstName = edtFirstName.textToString()
        requestModel.lastName = edtLastName.textToString()
        requestModel.password = edtPassword.textToString()
        (activity as ShopHopAppBaseActivity).registerUser(requestModel, false)
//        activity!!.launchActivity<ShopHopOTPActivity>()
        (activity as ShopHopSignInUpActivity).loadSignInFragment()

    }

    private fun validate(): Boolean {
        return when {
            edtFirstName.checkIsEmpty() -> {
                edtFirstName.showError(getString(R.string.sh_error_field_required))
                false
            }
            edtLastName.checkIsEmpty() -> {
                edtLastName.showError(getString(R.string.sh_error_field_required))
                false
            }
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
            edtConfirmPassword.checkIsEmpty() -> {
                edtConfirmPassword.showError(getString(R.string.sh_error_field_required))
                false
            }
            !edtPassword.text.toString().equals(edtConfirmPassword.text.toString(), false) -> {
                edtConfirmPassword.showError(getString(R.string.sh_error_password_not_matches))
                false
            }
            else -> true
        }
    }
}