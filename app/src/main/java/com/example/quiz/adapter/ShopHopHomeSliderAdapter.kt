package com.example.quiz.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.example.quiz.R
import com.example.quiz.utils.extensions.loadImageFromUrl
import kotlinx.android.synthetic.main.shophop_item_slider.view.imgSlider

class ShopHopHomeSliderAdapter(private var mImg: ArrayList<String>) : PagerAdapter() {
    var size: Int? = null

    override fun instantiateItem(parent: ViewGroup, position: Int): Any {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.shophop_item_slider, parent, false)

        view.imgSlider.loadImageFromUrl(mImg[position])

        parent.addView(view)
        return view
    }

    override fun isViewFromObject(v: View, `object`: Any): Boolean = v === `object` as View

    override fun getCount(): Int = mImg.size

    override fun destroyItem(parent: ViewGroup, position: Int, `object`: Any) =
        parent.removeView(`object` as View)

}
