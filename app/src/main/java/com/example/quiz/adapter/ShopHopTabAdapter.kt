package com.example.quiz.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.quiz.fragments.*

class ShopHopTabAdapter(fm: FragmentManager, val array: Array<ShopHopBaseFragment>) :
    FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return array[position]
    }
    override fun getCount(): Int {
        return array.size
    }
}