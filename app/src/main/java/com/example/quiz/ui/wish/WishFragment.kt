package com.example.quiz.ui.wish

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.quiz.R
import com.example.quiz.databinding.FragmentSignInBinding
import com.example.quiz.databinding.FragmentWishListBinding
import com.example.quiz.utils.extensions.onClick
import kotlinx.android.synthetic.main.fragment_sign_in.*

class WishFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentWishListBinding>(
            inflater,
            R.layout.fragment_wish_list, container, false
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}