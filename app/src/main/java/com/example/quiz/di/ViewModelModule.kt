package com.example.quiz.di

import com.example.quiz.ui.splash.SplashViewModel
import com.example.quiz.ui.walk.WalkThroughViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { SplashViewModel() }
    viewModel { WalkThroughViewModel() }
}