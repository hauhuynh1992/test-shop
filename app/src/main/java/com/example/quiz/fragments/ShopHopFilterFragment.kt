package com.example.quiz.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.example.quiz.R
import com.example.quiz.base.BaseRecyclerAdapter
import com.example.quiz.models.*
import com.example.quiz.utils.ShopHopConstants
import com.example.quiz.utils.extensions.*
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import com.example.quiz.databinding.*
import kotlinx.android.synthetic.main.shophop_layout_colors.*
import kotlinx.android.synthetic.main.shophop_layout_filter.*
import kotlinx.android.synthetic.main.shophop_layout_size.*


class ShopHopFilterFragment : BottomSheetDialogFragment() {

    companion object {
        fun newInstance(
            map: HashMap<String, ArrayList<Attribute>>,
            categoryList: ArrayList<CategoryData>
        ): ShopHopFilterFragment = ShopHopFilterFragment().apply {
            val bundle = Bundle()
            bundle.putSerializable(ShopHopConstants.KeyIntent.DATA, map)
            bundle.putSerializable(ShopHopConstants.KeyIntent.CATEGORY_DATA, categoryList)
            arguments = bundle
        }
    }

    private lateinit var map: HashMap<String, ArrayList<Attribute>>
    private val priceArray =
        arrayOf("$0", "$2", "$5", "$7", "$10", "$12", "$15", "$17", "$20", "$25", "$30")
    private val priceArray2 = arrayOf("0", "2", "5", "7", "10", "12", "15", "17", "20", "25", "30")
    private val ratings = arrayOf("4 star & above", "3 star & above", "2 star & above", "1 star & above")
    private lateinit var categoryList: ArrayList<CategoryData>
    private val brandColors = arrayOf(
        R.color.ShopHop_cat_1,
        R.color.ShopHop_cat_2,
        R.color.ShopHop_cat_3,
        R.color.ShopHop_cat_4
    )
    var mListener: FilterListener? = null

    private var subCategoryAdapter = object : BaseRecyclerAdapter<CategoryData, ShophopItemFilterCategoryBinding>() {
        override val layoutResId: Int = R.layout.shophop_item_filter_category

        override fun onBindData(model: CategoryData, position: Int, dataBinding: ShophopItemFilterCategoryBinding) {
            if (model.isSelected) {
                dataBinding.tvSubCategory.setTextColor(activity!!.color(R.color.ShopHop_white))
                dataBinding.tvSubCategory.setStrokedBackground(activity!!.color(R.color.ShopHop_colorPrimary))
            } else {
                dataBinding.tvSubCategory.setTextColor(activity!!.color(R.color.ShopHop_colorPrimary))
                dataBinding.tvSubCategory.setStrokedBackground(
                    activity!!.color(R.color.ShopHop_white),
                    activity!!.color(R.color.ShopHop_colorPrimary)
                )
            }
        }

        override fun onItemClick(view: View, model: CategoryData, position: Int, dataBinding: ShophopItemFilterCategoryBinding) {
            mModelList[position].isSelected = !(model.isSelected)
            notifyItemChanged(position)
        }

        override fun onItemLongClick(view: View, model: CategoryData, position: Int) {

        }
    }

    private var brandAdapter = object : BaseRecyclerAdapter<Brand, ShophopItemFilterBrandBinding>() {
        override val layoutResId: Int = R.layout.shophop_item_filter_brand

        override fun onBindData(model: Brand, position: Int, dataBinding: ShophopItemFilterBrandBinding) {
            when {
                model.isSelected!! -> {
                    dataBinding.tvBrandName.setTextColor(activity!!.color(model.color!!))
                    dataBinding.ivSelect.setImageResource(R.drawable.shophop_ic_check)
                    dataBinding.ivSelect.setColorFilter(activity!!.color(model.color!!))
                    dataBinding.ivSelect.setStrokedBackground(
                        activity!!.color(model.color!!),
                        activity!!.color(model.color!!),
                        0.4f
                    )
                }
                else -> {
                    dataBinding.tvBrandName.setTextColor(activity!!.color(R.color.ShopHop_textColorSecondary))
                    dataBinding.ivSelect.setImageResource(0)
                    dataBinding.ivSelect.setStrokedBackground(activity!!.color(R.color.ShopHop_checkbox_color))
                }
            }
        }

        override fun onItemClick(view: View, model: Brand, position: Int, dataBinding: ShophopItemFilterBrandBinding) {
            mModelList[position].isSelected = !(model.isSelected!!)
            notifyItemChanged(position)
        }

        override fun onItemLongClick(view: View, model: Brand, position: Int) {

        }
    }

    private var colorAdapter = object : BaseRecyclerAdapter<Color, ShophopItemColorBinding>() {
        override val layoutResId: Int = R.layout.shophop_item_color

        override fun onBindData(model: Color, position: Int, dataBinding: ShophopItemColorBinding) {
            dataBinding.viewColor.changeBackgroundTint(model.colorName!!)
            dataBinding.ivChecked.changeBackgroundTint(model.colorName!!)
            when {
                model.isSelected!! -> {
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
            mModelList[position].isSelected = !(model.isSelected!!)
            notifyItemChanged(position)
        }

        override fun onItemLongClick(view: View, model: Color, position: Int) {

        }
    }

    private var sizeAdapter = object : BaseRecyclerAdapter<Size, ShophopItemSizeBinding>() {
        override val layoutResId: Int = R.layout.shophop_item_size

        override fun onBindData(model: Size, position: Int, dataBinding: ShophopItemSizeBinding) {
            dataBinding.ivChecked.text = model.sizeName
            when {
                model.isSelected!! -> {
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
            mModelList[position].isSelected = !(model.isSelected!!)
            notifyItemChanged(position)
        }

        override fun onItemLongClick(view: View, model: Size, position: Int) {

        }
    }

    private var discountAdapter = getExpandableAdapter()

    private var ratingAdapter = getExpandableAdapter()

    private var rotateAnimation: RotateAnimation? = getRotationAnimation(0f, 180f)
    private var rotateAnimation2: RotateAnimation? = getRotationAnimation(180f, 360f)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.shophop_layout_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        map = arguments?.getSerializable(ShopHopConstants.KeyIntent.DATA) as HashMap<String, ArrayList<Attribute>>
        categoryList = arguments?.getSerializable(ShopHopConstants.KeyIntent.CATEGORY_DATA) as ArrayList<CategoryData>
        bindAdapters()
        bindData()
        bindClickListeners()
    }

    private fun bindClickListeners() {
        ivClose.onClick {
            dismiss()
        }
        rlDiscount.onClick {
            if (rcvDiscount.isVisible()) {
                ivExpandDiscount.startAnimation(rotateAnimation)
                invalidateView(rcvDiscount, false)
            } else {
                ivExpandDiscount.startAnimation(rotateAnimation2)
                invalidateView(rcvDiscount, true)

            }
        }
        rlRatings.onClick {
            if (rcvRatings.isVisible()) {
                ivExpandRating.startAnimation(rotateAnimation)
                invalidateView(rcvRatings, false)
            } else {
                ivExpandRating.startAnimation(rotateAnimation2)
                invalidateView(rcvRatings, true)
            }
        }
        tvSelectAll.onClick {
            brandAdapter.mModelList.forEach {
                it.isSelected = true
            }
            brandAdapter.notifyDataSetChanged()
        }
        tvShowMore.onClick {
            if (subCategoryAdapter.size == 5) {
                //expand
                subCategoryAdapter.setModelSize(categoryList.size)
                tvShowMore.text = context.getString(R.string.sh_lbl_less)
            } else {
                //collapse
                subCategoryAdapter.setModelSize(5)
                tvShowMore.text = context.getString(R.string.sh_lbl_more)
            }
            subCategoryAdapter.notifyDataSetChanged()

        }
        tvReset.onClick {
            resetSubcategory()
            resetPrice()
            resetBrands()
            resetColors()
            resetSize()
            resetDiscount()
            resetRatings()
        }
        tvApply.onClick {
            if (mListener != null) {

                val brands = ArrayList<String>()
                brandAdapter.mModelList.forEach {
                    if (it.isSelected!!) {
                        brands.add(it.brandName!!)
                    }
                }
                Log.e("brands", Gson().toJson(brands))

                val colors = ArrayList<String>()
                colorAdapter.mModelList.forEach {
                    if (it.isSelected!!) {
                        colors.add(it.color!!)
                    }
                }

                Log.e("colors", Gson().toJson(colors))

                val sizes = ArrayList<String>()
                sizeAdapter.mModelList.forEach {
                    if (it.isSelected!!) {
                        sizes.add(it.sizeName!!)
                    }
                }

                val cateogry = ArrayList<String>()
                subCategoryAdapter.mModelList.forEach {
                    if (it.isSelected) {
                        cateogry.add(it.name)
                    }
                }

                Log.e("cateogry", Gson().toJson(cateogry))
                mListener?.apply(
                    priceArray2[rangebar1.leftPinValue.toInt()].toInt(),
                    priceArray2[rangebar1.rightPinValue.toInt()].toInt(),
                    brands,
                    colors,
                    sizes,
                    cateogry
                )
            }
            dismiss()
        }
    }

    private fun resetRatings() {
        ratingAdapter.mModelList.forEach {
            it.isSelected = false
        }
        ratingAdapter.notifyDataSetChanged()
    }

    private fun resetDiscount() {
        discountAdapter.mModelList.forEach {
            it.isSelected = false
        }
        discountAdapter.notifyDataSetChanged()
    }

    private fun resetSize() {
        sizeAdapter.mModelList.forEach {
            it.isSelected = false
        }
        sizeAdapter.notifyDataSetChanged()
    }

    private fun resetColors() {
        colorAdapter.mModelList.forEach {
            it.isSelected = false
        }
        colorAdapter.notifyDataSetChanged()

    }

    private fun resetBrands() {
        brandAdapter.mModelList.forEach {
            it.isSelected = false
        }
        brandAdapter.notifyDataSetChanged()
    }

    private fun resetPrice() {

    }

    private fun resetSubcategory() {
        subCategoryAdapter.mModelList.forEach {
            it.isSelected = false
        }
        subCategoryAdapter.notifyDataSetChanged()
    }

    private fun invalidateView(recyclerView: RecyclerView?, b: Boolean) {
        runDelayed(200) {
            if (!b) {
                recyclerView?.hide()
            } else {
                recyclerView?.show()
            }
        }
    }

    private fun getRotationAnimation(from: Float, to: Float): RotateAnimation? {
        val animation = RotateAnimation(from, to, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        animation.duration = 300
        animation.fillAfter = true
        animation.interpolator = LinearInterpolator()
        return animation
    }

    private fun bindData() {
        if (rangebar1 != null) {
            rangebar1.tickTopLabels = priceArray

            subCategoryAdapter.setModelSize(5)
            subCategoryAdapter.addItems(categoryList)
            if (map.containsKey("Brand")) {
                val brandList = ArrayList<Brand>()
                map["Brand"]?.forEachIndexed { index, s ->
                    val brand = Brand()
                    brand.brandName = s.name
                    brand.id = s.id
                    brand.color = brandColors[index % brandColors.size]
                    brandList.add(brand)
                }
                brandAdapter.addItems(brandList)
            }
            if (map.containsKey("Color")) {
                val colorList = ArrayList<Color>()
                map["Color"]?.forEach { s ->
                    if (s.name.isValidColor()) {
                        val color = Color()
                        color.colorName = android.graphics.Color.parseColor(s.name)
                        color.id = s.id
                        color.color=s.name
                        colorList.add(color)
                    }
                }
                colorAdapter.addItems(colorList)
            }
            if (map.containsKey("Size")) {
                val sizeList = ArrayList<Size>()
                map["Size"]?.forEach { s ->
                    val size = Size()
                    size.sizeName = s.name
                    size.id = s.id
                    size.isSelected = false
                    sizeList.add(size)
                }
                sizeAdapter.addItems(sizeList)
            }

            val ratingList = ArrayList<Discount>()

            ratings.forEach {
                val size = Discount()
                size.discount = it
                ratingList.add(size)
            }
            ratingAdapter.addItems(ratingList)
        }

    }

    private fun bindAdapters() {
        val layoutManager = ChipsLayoutManager.newBuilder(activity)
            .setOrientation(ChipsLayoutManager.HORIZONTAL)
            .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
            .withLastRow(false)
            .build()
        rcvSubCategories.layoutManager = layoutManager
        rcvSubCategories.itemAnimator = null
        rcvSubCategories.adapter = subCategoryAdapter

        rcvBrands.setVerticalLayout()
        rcvColors.setHorizontalLayout()
        rcvSize.setHorizontalLayout()
        rcvDiscount.setVerticalLayout()
        rcvRatings.setVerticalLayout()

        rcvBrands.adapter = brandAdapter
        rcvColors.adapter = colorAdapter
        rcvSize.adapter = sizeAdapter
        rcvDiscount.adapter = discountAdapter
        rcvRatings.adapter = ratingAdapter
    }

    private fun getExpandableAdapter(): BaseRecyclerAdapter<Discount, ShophopItemFilterDiscountBinding> {
        return object : BaseRecyclerAdapter<Discount, ShophopItemFilterDiscountBinding>() {
            override val layoutResId: Int = R.layout.shophop_item_filter_discount

            override fun onBindData(model: Discount, position: Int, dataBinding: ShophopItemFilterDiscountBinding) {
                when {
                    model.isSelected!! -> {
                        dataBinding.tvDiscount.setTextColor(activity!!.color(R.color.ShopHop_textColorPrimary))
                        dataBinding.ivSelect.setImageResource(R.drawable.shophop_ic_check)
                        dataBinding.ivSelect.setStrokedBackground(
                            activity!!.color(R.color.ShopHop_textColorPrimary),
                            activity!!.color(R.color.ShopHop_textColorPrimary),
                            0.4f
                        )
                    }
                    else -> {
                        dataBinding.tvDiscount.setTextColor(activity!!.color(R.color.ShopHop_textColorSecondary))
                        dataBinding.ivSelect.setImageResource(0)
                        dataBinding.ivSelect.changeBackgroundTint(activity!!.color(R.color.ShopHop_checkbox_color))
                    }
                }
            }

            override fun onItemClick(
                view: View,
                model: Discount,
                position: Int,
                dataBinding: ShophopItemFilterDiscountBinding
            ) {
                mModelList[position].isSelected = !(model.isSelected!!)
                notifyItemChanged(position)
            }

            override fun onItemLongClick(view: View, model: Discount, position: Int) {

            }
        }
    }

    fun invalidate(map: HashMap<String, ArrayList<Attribute>>, categoryList: ArrayList<CategoryData>) {
        this.map = map
        this.categoryList = categoryList
        bindData()
    }

    interface FilterListener {
        fun apply(
            minPrice: Int,
            maxPrice: Int,
            selectedBrands: ArrayList<String>,
            selectedColors: ArrayList<String>,
            selectedSize: ArrayList<String>,
            selectedCategory: ArrayList<String>
        )
    }

}