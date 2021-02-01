package com.example.quiz.ui.walk

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.quiz.R
import com.example.quiz.databinding.FragmentWalkThroughBinding
import com.example.quiz.ui.walk.adapter.WalkAdapter
import com.example.quiz.utils.ShopHopCarouselEffectTransformer
import com.example.quiz.utils.extensions.onClick
import com.example.quiz.utils.extensions.onPageSelected
import kotlinx.android.synthetic.main.fragment_walk_through.*

class WalkThroughFragment : Fragment() {


    var mCount: Int? = null
    var mHeading = arrayOf("Hi, We're Woobox!", "Most Unique Styles!", "Shop Till You Drop!")
    private val mSubHeading = arrayOf(
        "We make around your city Affordable,\n easy and efficient.",
        "Shop the most trending fashion on the biggest shopping website.",
        "Grab the best seller pieces at bargain prices."
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentWalkThroughBinding>(
            inflater,
            R.layout.fragment_walk_through, container, false
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        init()
        val adapter = WalkAdapter()

        ViewPager.adapter = adapter
        dots.attachViewPager(ViewPager)
        dots.setDotDrawable(R.drawable.shophop_bg_circle_primary, R.drawable.shophop_bg_black_dot)
        mCount = adapter.count
        btnStatShopping.onClick {
            findNavController().navigate(R.id.action_walkThroughFragment_to_dashboardFragment)
        }


        llSignIn.onClick {
            findNavController().navigate(R.id.action_walkThroughFragment_to_signInFragment)
        }
    }

    private fun init() {
        ViewPager.apply {
            clipChildren = false
            pageMargin = resources.getDimensionPixelOffset(R.dimen.sh_spacing_small)
            offscreenPageLimit = 3
            setPageTransformer(
                false,
                ShopHopCarouselEffectTransformer(requireContext())
            )
            offscreenPageLimit = 0

            onPageSelected { position: Int ->
                val animFadeIn = android.view.animation.AnimationUtils.loadAnimation(
                    requireContext(),
                    R.anim.shophop_fade_in
                )
                tvHeading.startAnimation(animFadeIn)
                tvSubHeading.startAnimation(animFadeIn)
                tvHeading.text = mHeading[position]
                tvSubHeading.text = mSubHeading[position]
            }
        }
    }
}