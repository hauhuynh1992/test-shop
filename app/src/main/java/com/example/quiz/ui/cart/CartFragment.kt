package com.example.quiz.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.quiz.R
import com.example.quiz.databinding.FragmentCartBinding
import com.example.quiz.databinding.FragmentSignInBinding
import com.example.quiz.utils.extensions.onClick
import kotlinx.android.synthetic.main.fragment_sign_in.*

class CartFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentCartBinding>(
            inflater,
            R.layout.fragment_cart, container, false
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

}