package com.example.quiz.ui.sign_in

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.quiz.R
import com.example.quiz.databinding.FragmentSignInBinding
import com.example.quiz.utils.extensions.onClick
import kotlinx.android.synthetic.main.fragment_sign_in.*

class SignInFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentSignInBinding>(
            inflater,
            R.layout.fragment_sign_in, container, false
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btnSignIn.onClick {
            findNavController().navigate(R.id.action_signInFragment_to_dashboardFragment)
        }

        btnSignUp.onClick {
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }
    }

}