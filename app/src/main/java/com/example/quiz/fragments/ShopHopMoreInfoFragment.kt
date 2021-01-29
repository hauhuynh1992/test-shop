package com.example.quiz.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.quiz.R
import com.example.quiz.databinding.ShophopFragmentItemmoreinfoBinding
import com.example.quiz.models.Attributes
import com.example.quiz.models.ProductModel
import com.example.quiz.utils.ShopHopConstants
import kotlinx.android.synthetic.main.shophop_fragment_itemmoreinfo.*


class ShopHopMoreInfoFragment : ShopHopBaseFragment() {
    companion object {
        fun getNewInstance(model: ProductModel): ShopHopMoreInfoFragment {
            val fragment = ShopHopMoreInfoFragment()
            val bundle = Bundle()
            bundle.putSerializable(ShopHopConstants.KeyIntent.DATA, model)
            fragment.arguments = bundle
            return fragment
        }
    }

    val sizeList = ArrayList<String>()
    lateinit var model: ProductModel
    lateinit var mainBinding: ShophopFragmentItemmoreinfoBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        mainBinding = DataBindingUtil.inflate(inflater, R.layout.shophop_fragment_itemmoreinfo, container, false)
        return mainBinding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model = arguments?.getSerializable(ShopHopConstants.KeyIntent.DATA) as ProductModel

        mainBinding.model = model

        if (model.dimensions.length.isNotEmpty())
            tvLength.text = model.dimensions.length + " cm"
        if (model.dimensions.height.isNotEmpty())
            tvHeight.text = model.dimensions.height + " cm"
        if (model.dimensions.width.isNotEmpty())
            tvWidth.text = model.dimensions.width + " cm"
        //  tvBrandName.op
        val builder = StringBuffer()
        if (model.attributes.isNotEmpty()) {
            model.attributes.forEach{ attributes: Attributes ->
                if (attributes.name == "Brand") {
                    attributes.options.forEachIndexed {index:Int,option: String ->
                        sizeList.add(option)
                        builder.append(option)
                        if (index<attributes.options.size-1){
                            builder.append("\n")
                        }
                    }
                }
            }
            tvBrandName.text = builder.toString()
        }
    }
}