package com.example.quiz.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.quiz.R
import kotlinx.android.synthetic.main.shophop_spinner_language.view.*

class ShopHopLanguageAdapter(internal var context: Context, private var Flag: IntArray, private var LanguageName: Array<String>) : BaseAdapter() {

    override fun getCount(): Int {
        return Flag.size
    }

    override fun getItem(i: Int): Any? {
        return null
    }

    override fun getItemId(i: Int): Long {
        return 0
    }

    @SuppressLint("ViewHolder", "InflateParams")
    override fun getView(i: Int, view: View?, viewGroup: ViewGroup): View {
        return LayoutInflater.from(context).inflate(R.layout.shophop_spinner_language, null).apply {
            ivLogo.setImageResource(Flag[i])
            tvName.text = LanguageName[i]
        }


    }
}