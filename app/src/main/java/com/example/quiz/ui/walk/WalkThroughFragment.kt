package com.example.quiz.ui.walk

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.example.quiz.R
import com.example.quiz.activity.ShopHopDashBoardActivity
import com.example.quiz.activity.ShopHopSignInUpActivity
import com.example.quiz.databinding.FragmentWalkThroughBinding
import com.example.quiz.ui.base.BaseFragment
import com.example.quiz.ui.walk.adapter.WalkAdapter
import com.example.quiz.utils.ShopHopCarouselEffectTransformer
import com.example.quiz.utils.ShopHopConstants
import com.example.quiz.utils.extensions.getSharedPrefInstance
import com.example.quiz.utils.extensions.launchActivity
import com.example.quiz.utils.extensions.onClick
import com.example.quiz.utils.extensions.onPageSelected
import kotlinx.android.synthetic.main.fragment_walk_through.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class WalkThroughFragment : BaseFragment<FragmentWalkThroughBinding, WalkThroughViewModel>() {

    override val viewModel: WalkThroughViewModel by viewModel()
    override val layoutId: Int
        get() = R.layout.fragment_walk_through

    var mCount: Int? = null
    var mHeading = arrayOf("Hi, We're Woobox!", "Most Unique Styles!", "Shop Till You Drop!")
    private val mSubHeading = arrayOf(
        "We make around your city Affordable,\n easy and efficient.",
        "Shop the most trending fashion on the biggest shopping website.",
        "Grab the best seller pieces at bargain prices."
    )

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
        val adapter = WalkAdapter()

        ViewPager.adapter = adapter
        dots.attachViewPager(ViewPager)
        dots.setDotDrawable(R.drawable.shophop_bg_circle_primary, R.drawable.shophop_bg_black_dot)
        mCount = adapter.count
//        if (getSharedPrefInstance().getBooleanValue(ShopHopConstants.SharedPref.SHOW_SWIPE)) {
//            launchActivity<ShopHopDashBoardActivity>()
//            finish()
//        }
        btnStatShopping.onClick {
//            launchActivity<ShopHopDashBoardActivity>()
//            getSharedPrefInstance().setValue(ShopHopConstants.SharedPref.SHOW_SWIPE, true)
//            finish()
        }
        llSignIn.onClick {
//            launchActivity<ShopHopSignInUpActivity>()
        }

        btnStatShopping.setOnClickListener {
            findNavController().navigate(R.id.action_walkThroughFragment_to_dashboardFragment)
        }
    }

    private fun init() {
        ViewPager.apply {
            clipChildren = false
            pageMargin = resources.getDimensionPixelOffset(R.dimen.sh_spacing_small)
            offscreenPageLimit = 3
            setPageTransformer(false,
                ShopHopCarouselEffectTransformer(requireContext())
            )
            offscreenPageLimit = 0

            onPageSelected { position: Int ->
                val animFadeIn = android.view.animation.AnimationUtils.loadAnimation(requireContext(), R.anim.shophop_fade_in)
                tvHeading.startAnimation(animFadeIn)
                tvSubHeading.startAnimation(animFadeIn)
                tvHeading.text = mHeading[position]
                tvSubHeading.text = mSubHeading[position]
            }
        }
    }
}