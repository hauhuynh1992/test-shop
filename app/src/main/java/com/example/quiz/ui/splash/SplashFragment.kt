package com.example.quiz.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.quiz.R
import com.example.quiz.activity.ShopHopWalkThroughActivity
import com.example.quiz.databinding.FragmentSplashBinding
import com.example.quiz.utils.extensions.launchActivity
import com.example.quiz.utils.extensions.runDelayed

class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentSplashBinding>(
            inflater,
            R.layout.fragment_splash, container, false
        )

        val w = requireActivity().window
        w.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        runDelayed(500) {
            findNavController().navigate(R.id.action_splashFragment_to_walkThroughFragment)
        }

    }
}