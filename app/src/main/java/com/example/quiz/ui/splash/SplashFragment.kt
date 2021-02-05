package com.example.quiz.ui.splash

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.example.quiz.R
import com.example.quiz.databinding.FragmentSplashBinding
import com.example.quiz.ui.base.BaseFragment
import com.example.quiz.utils.extensions.runDelayed
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashFragment : BaseFragment<FragmentSplashBinding, SplashViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_splash

    override val viewModel: SplashViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        runDelayed(500) {
            findNavController().navigate(R.id.action_splashFragment_to_walkThroughFragment)
        }
    }


}