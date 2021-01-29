package com.example.quiz.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.quiz.R
import com.example.quiz.base.BaseRecyclerAdapter
import com.example.quiz.databinding.*
import com.example.quiz.models.Color
import com.example.quiz.models.ProductModel
import com.example.quiz.models.Size
import com.example.quiz.utils.ShopHopConstants
import com.example.quiz.utils.extensions.*
import kotlinx.android.synthetic.main.shophop_fragment_itemdecription.*


class ShopHopItemDescriptionFragment : ShopHopBaseFragment() {

    companion object {
        fun getNewInstance(model: ProductModel): ShopHopItemDescriptionFragment {
            val fragment = ShopHopItemDescriptionFragment()
            val bundle = Bundle()
            bundle.putSerializable(ShopHopConstants.KeyIntent.DATA, model)
            fragment.arguments = bundle
            return fragment
        }
    }

    private var mColorFlag: Int = -1
    private var mSizeFlag: Int = -1
    lateinit var model: ProductModel
    lateinit var mainBinding: ShophopFragmentItemdecriptionBinding


    private var colorAdapter = object : BaseRecyclerAdapter<Color, ShophopItemColorBinding>() {

        override val layoutResId: Int = R.layout.shophop_item_color

        override fun onBindData(model: Color, position: Int, dataBinding: ShophopItemColorBinding) {
            dataBinding.viewColor.setStrokedBackground(android.graphics.Color.parseColor(model.color!!),android.graphics.Color.BLACK)
            dataBinding.ivChecked.setStrokedBackground(android.graphics.Color.parseColor(model.color!!),android.graphics.Color.BLACK)
            when {
                position == mColorFlag -> {
                    dataBinding.viewColor.hide()
                    dataBinding.ivChecked.show()

                }
                else -> {
                    dataBinding.viewColor.show()
                    dataBinding.ivChecked.hide()
                }
            }
        }

        override fun onItemClick(view: View, model: Color, position: Int, dataBinding: ShophopItemColorBinding) {
            mColorFlag = position
            notifyDataSetChanged()
            getSelectedColors()
        }

        override fun onItemLongClick(view: View, model: Color, position: Int) {

        }

    }

    private var sizeAdapter = object : BaseRecyclerAdapter<Size, ShophopItemSizeBinding>() {
        override val layoutResId: Int = R.layout.shophop_item_size

        override fun onBindData(model: Size, position: Int, dataBinding: ShophopItemSizeBinding) {
            dataBinding.ivChecked.text = model.size
            when {
                position == mSizeFlag -> {
                    dataBinding.ivChecked.setTextColor(activity!!.color(R.color.ShopHop_white))
                    dataBinding.ivChecked.background =
                            ContextCompat.getDrawable(activity!!, R.drawable.shophop_bg_circle_primary)
                }
                else -> {
                    dataBinding.ivChecked.setTextColor(activity!!.color(R.color.ShopHop_textColorPrimary))
                    dataBinding.ivChecked.background = null
                }
            }
        }

        override fun onItemClick(view: View, model: Size, position: Int, dataBinding: ShophopItemSizeBinding) {
            mSizeFlag = position
            notifyDataSetChanged()
            getSelectedSize()
        }

        override fun onItemLongClick(view: View, model: Size, position: Int) {

        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mainBinding = DataBindingUtil.inflate(inflater, R.layout.shophop_fragment_itemdecription, container, false)
        return mainBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model = arguments?.getSerializable(ShopHopConstants.KeyIntent.DATA) as ProductModel

        mainBinding.model = model

        tvFreePrice.applyStrike()
        tvFreePrice.text = model.price.currencyFormat()
        bindAdapters()
        bindData()
    }

    private fun bindData() {
        txtDescription.setText(model.short_description.getHtmlString().toString())
        val colorList = ArrayList<Color>()
        val sizeList = ArrayList<Size>()

        if (model.attributes.isNotEmpty()) {
            model.attributes.forEach {
                if (it.name == "Size") {
                    it.options.forEach { option ->
                        val mSize = Size()
                        mSize.size = option
                        sizeList.add(mSize)
                    }
                } else if (it.name == "Color") {
                    it.options.forEach { option ->
                        val color = Color()
                        color.color = option
                        colorList.add(color)
                    }
                }
            }

            if (sizeList.size == 0) {
                tvSize.hide()
            } else {
                sizeAdapter.addItems(sizeList)
            }

            if (colorList.size == 0) {
                tvcolor.hide()
            } else {
                colorAdapter.addItems(colorList)
            }
        }

    }

    private fun bindAdapters() {
        rvColors.setHorizontalLayout()
        rvSize.setHorizontalLayout()

        rvColors.adapter = colorAdapter
        rvSize.adapter = sizeAdapter
    }

    fun getSelectedColors(): String {
        return if (mColorFlag == -1) {
            ""
        } else {
            colorAdapter.mModelList[mColorFlag].color!!
        }
    }

    fun getSelectedSize(): String {
        return if (mSizeFlag == -1) {
            ""
        } else {
            sizeAdapter.mModelList[mSizeFlag].size!!
        }
    }

    fun isColorAvailable(): Boolean {
        return colorAdapter.mModelList.size != 0
    }

    fun isSizeAvailable(): Boolean {
        return sizeAdapter.mModelList.size != 0
    }


}