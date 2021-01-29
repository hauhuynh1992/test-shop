package com.example.quiz.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.example.quiz.ShopHopAppBaseActivity
import com.example.quiz.R
import com.example.quiz.activity.ShopHopMyCartActivity
import com.example.quiz.activity.ShopHopSearchActivity
import com.example.quiz.base.BaseRecyclerAdapter
import com.example.quiz.databinding.ShophopItemNewestProductBinding
import com.example.quiz.databinding.ShophopItemViewproductgridBinding
import com.example.quiz.models.Attribute
import com.example.quiz.models.CategoryData
import com.example.quiz.models.ProductModel
import com.example.quiz.utils.ShopHopConstants
import com.example.quiz.utils.ShopHopConstants.viewAllCode.FEATURED
import com.example.quiz.utils.ShopHopConstants.viewAllCode.NEWEST
import com.example.quiz.utils.ShopHopHidingScrollListener
import com.example.quiz.utils.extensions.*
import kotlinx.android.synthetic.main.shophop_fragment_newest_product.*
import kotlinx.android.synthetic.main.shophop_layout_color.view.*
import kotlinx.android.synthetic.main.shophop_menu_cart.view.*


class ShopHopViewAllProductFragment : ShopHopBaseFragment() {
    private lateinit var mProductModel: ProductModel

    companion object {
        fun getNewInstance(id: Int, mCategoryId: String?): ShopHopViewAllProductFragment {
            val fragment = ShopHopViewAllProductFragment()
            val bundle = Bundle()
            bundle.putSerializable(ShopHopConstants.KeyIntent.VIEWALLID, id)
            bundle.putSerializable(ShopHopConstants.KeyIntent.KEYID, mCategoryId)

            fragment.arguments = bundle
            return fragment
        }
    }

    private val mListAdapter = getAdapter()
    private val mGridAdapter = getGridAdapter()
    private var mFlag: Boolean = true
    private var menuCart: View? = null
    private var mId: Int = 0
    private var mCategoryId: String? = ""
    private var filterFragment: ShopHopFilterFragment? = null
    val listener = object : ShopHopFilterFragment.FilterListener {
        override fun apply(
                minPrice: Int,
                maxPrice: Int,
                selectedBrands: ArrayList<String>,
                selectedColors: ArrayList<String>,
                selectedSize: ArrayList<String>,
                selectedCategory: ArrayList<String>
        ) {
            val map = HashMap<String, ArrayList<String>>()

            if (selectedBrands.size > 0) {
                map["Brand"] = selectedBrands
            }
            if (selectedSize.size > 0) {
                map["Size"] = selectedSize
            }
            if (selectedColors.size > 0) {
                map["Color"] = selectedColors
            }
            mListAdapter.clearData()
            mGridAdapter.clearData()
            list.forEach {
                if (it.regular_price.toInt() in minPrice..maxPrice && catExist(
                                it,
                                selectedCategory
                        ) && checkIfExist(it, map)
                ) {
                    mListAdapter.addNewItem(it)
                    mGridAdapter.addNewItem(it)
                }
            }
        }

    }

    private fun checkIfExist(
            model: ProductModel,
            map: HashMap<String, ArrayList<String>>
    ): Boolean {
        if (map.size == 0) {
            return true
        }
        map.keys.forEach {
            model.attributes.forEach { attrType ->
                if (attrType.name == it) {
                    map[it]!!.forEach { filterAttr ->
                        if (attrType.options.contains(filterAttr)) {
                            return true
                        }
                    }
                }
            }
        }
        return false
    }

    private fun catExist(model: ProductModel, map: ArrayList<String>): Boolean {
        if (map.size == 0) {
            return true
        }
        model.categories.forEach { category ->
            if (map.contains(category.name)) {
                return true
            }
        }
        return false
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.shophop_fragment_newest_product, container, false)
    }

    var isFeatured = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        setClickEventListener()
        rvNewestProduct.adapter = mListAdapter
        rvNewestProduct.rvItemAnimation()
        rvNewestProduct.setOnScrollListener(object : ShopHopHidingScrollListener(activity) {

            override fun onMoved(distance: Int) {
                rlTop.translationY = -distance.toFloat()
            }

            override fun onShow() {
                rlTop.animate().translationY(0f).setInterpolator(DecelerateInterpolator(2f)).start()
            }

            override fun onHide() {
                rlTop.animate().translationY((-rlTop.height).toFloat())
                        .setInterpolator(AccelerateInterpolator(2f))
                        .start()
            }
        })

        mId = arguments!!.getInt(ShopHopConstants.KeyIntent.VIEWALLID)
        mCategoryId = arguments!!.getString(ShopHopConstants.KeyIntent.KEYID)
        when (mId) {
            ShopHopConstants.viewAllCode.RECENTSEARCH -> {
                mListAdapter.addItems(getRecentItems())
                mGridAdapter.addItems(getRecentItems())
            }
            ShopHopConstants.viewAllCode.CATEGORY_FEATURED -> {
                isFeatured = true
                getSubCategoryProducts(true)
            }
            ShopHopConstants.viewAllCode.CATEGORY_NEWEST -> {
                getSubCategoryProducts(false)
            }
            ShopHopConstants.viewAllCode.OFFERS -> {
                getOffers()
            }
            FEATURED -> {
                isFeatured = true
                listAllProducts(mId)
            }
            NEWEST -> {
                listAllProducts(mId)
            }
            else -> {
                listAllProducts(mId)
            }
        }
    }

    private fun getOffers() {
        listAllProducts {
            mListAdapter.clearData()
            mGridAdapter.clearData()
            list.clear()
            it.forEach { model ->
                if (model.on_sale) {
                    list.add(model)
                    mListAdapter.addNewItem(model)
                    mGridAdapter.addNewItem(model)
                }
            }
            getFilterAttributes()

        }
    }

    private var list = ArrayList<ProductModel>()


    private fun getAdapter(): BaseRecyclerAdapter<ProductModel, ShophopItemNewestProductBinding> {

        return object : BaseRecyclerAdapter<ProductModel, ShophopItemNewestProductBinding>() {

            override fun onItemClick(
                    view: View,
                    model: ProductModel,
                    position: Int,
                    dataBinding: ShophopItemNewestProductBinding
            ) {
                when (view.id) {
                    R.id.ivDislike -> {
                        (activity as ShopHopAppBaseActivity).addToWishList(model, onApiSuccess = {
                            dataBinding.ivDislike.hide()
                            dataBinding.ivlike.show()
                        })
                    }
                    R.id.ivlike -> {
                        dataBinding.ivDislike.show()
                        dataBinding.ivlike.hide()
                    }
                    R.id.listProductRaw -> {
                        (activity as ShopHopAppBaseActivity).showProductDetail(model)
                    }
                }
            }

            override val layoutResId: Int = R.layout.shophop_item_newest_product

            override fun onBindData(
                    model: ProductModel,
                    position: Int,
                    dataBinding: ShophopItemNewestProductBinding
            ) {
                val mStringBuffer = StringBuilder()
                mProductModel = model
                if (model.images.isNotEmpty()) {
                    dataBinding.ivProduct.loadImageFromUrl(model.images[0].src)
                }
                if (model.on_sale)
                    dataBinding.tvProductPrice.text = model.sale_price.currencyFormat()
                else
                    dataBinding.tvProductPrice.text = model.price.currencyFormat()

                dataBinding.tvProductActualPrice.text = model.regular_price.currencyFormat()
                dataBinding.tvProductActualPrice.applyStrike()
                dataBinding.llProductColor.removeAllViews()
                if (model.attributes.isNotEmpty()) {
                    for (i in 0 until model.attributes.size) {
                        if (model.attributes[i].name == "Size") {
                            model.attributes[i].options.forEach {
                                mStringBuffer.append("$it  ")
                            }
                            dataBinding.tvSize.text = mStringBuffer

                        }
                        if (model.attributes[i].name == "Color") {
                            model.attributes[i].options.forEach {
                                if (it.contains("#")) {
                                    val view1: View =
                                            layoutInflater.inflate(
                                                    R.layout.shophop_layout_color,
                                                    dataBinding.llProductColor,
                                                    false
                                            )

                                    view1.ivColor.changeBackgroundTint(Color.parseColor(it))
                                    dataBinding.llProductColor.addView(view1)
                                }
                            }
                        }
                    }
                }
            }

            override fun onItemLongClick(view: View, model: ProductModel, position: Int) {}
        }
    }

    private fun getGridAdapter(): BaseRecyclerAdapter<ProductModel, ShophopItemViewproductgridBinding> {

        return object : BaseRecyclerAdapter<ProductModel, ShophopItemViewproductgridBinding>() {

            override val layoutResId: Int = R.layout.shophop_item_viewproductgrid

            override fun onBindData(
                    model: ProductModel,
                    position: Int,
                    dataBinding: ShophopItemViewproductgridBinding
            ) {

                if (model.on_sale)
                    dataBinding.tvDiscountPrice.text = model.sale_price.currencyFormat()
                else
                    dataBinding.tvDiscountPrice.text = model.price.currencyFormat()
                dataBinding.tvOriginalPrice.text = model.regular_price.currencyFormat()
                dataBinding.tvOriginalPrice.applyStrike()
                if (model.images.isNotEmpty()) {
                    dataBinding.ivProduct.loadImageFromUrl(model.images[0].src)
                }
                dataBinding.llDynamicProductColor.removeAllViews()
                if (model.attributes.isNotEmpty()) {
                    model.attributes[0].options.forEach {
                        if (it.contains("#")) {
                            val view1: View =
                                    layoutInflater.inflate(
                                            R.layout.shophop_layout_color,
                                            dataBinding.llDynamicProductColor,
                                            false
                                    )

                            view1.ivColor.changeBackgroundTint(Color.parseColor(it))
                            dataBinding.llDynamicProductColor.addView(view1)
                        }
                    }
                }
            }

            override fun onItemClick(
                    view: View,
                    model: ProductModel,
                    position: Int,
                    dataBinding: ShophopItemViewproductgridBinding
            ) {
                when (view.id) {
                    R.id.ivFavourite -> mFlag = if (mFlag) {
                        (activity as ShopHopAppBaseActivity).addToWishList(model, onApiSuccess = {
                            dataBinding.ivFavourite.setImageResource(R.drawable.shophop_ic_heart_fill)
                            dataBinding.ivFavourite.applyColorFilter(activity!!.color(R.color.ShopHop_colorPrimary))
                            dataBinding.ivFavourite.setStrokedBackground(activity!!.color(R.color.ShopHop_favourite_background))
                        })
                        false
                    } else {
                        dataBinding.ivFavourite.setImageResource(R.drawable.shophop_ic_heart)

                        dataBinding.ivFavourite.applyColorFilter(activity!!.color(R.color.ShopHop_textColorSecondary))
                        dataBinding.ivFavourite.setStrokedBackground(activity!!.color(R.color.ShopHop_favourite_unselected_background))
                        true
                    }
                    R.id.gridProduct -> (activity as ShopHopAppBaseActivity).showProductDetail(model)
                }
            }

            override fun onItemLongClick(view: View, model: ProductModel, position: Int) {}
        }
    }

    private fun setClickEventListener() {
        ivGrid.onClick {
            setColorFilter(ContextCompat.getColor(context, R.color.ShopHop_colorPrimary))
            ivList.setColorFilter(activity!!.color(R.color.ShopHop_textColorSecondary))
            rvNewestProduct.apply {
                layoutManager = GridLayoutManager(activity, 2)
                setHasFixedSize(true)
                // mGridAdapter.addItems(arguments?.getSerializable(ShopHopConstants.KeyIntent.PRODUCTDATA) as java.util.ArrayList<ProductModel>)
                rvNewestProduct.adapter = mGridAdapter
                rvNewestProduct.rvItemAnimation()
            }

        }
        ivList.onClick {
            setColorFilter(ContextCompat.getColor(context, R.color.ShopHop_colorPrimary))
            ivGrid.setColorFilter(ContextCompat.getColor(context, R.color.ShopHop_textColorSecondary))
            rvNewestProduct.apply {
                layoutManager = LinearLayoutManager(activity)
                setHasFixedSize(true)
                rvNewestProduct.adapter = mListAdapter
                rvNewestProduct.rvItemAnimation()
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.shophop_menu_dashboard, menu)
        val menuItem = menu.findItem(R.id.action_filter)
        val menuWishItem = menu.findItem(R.id.action_cart)
        if (mId != ShopHopConstants.viewAllCode.RECENTSEARCH) {
            menuItem.isVisible = true
        }
        menuWishItem.isVisible = true
        menuCart = menuWishItem.actionView
        menuCart!!.ivCart.setColorFilter(activity!!.color(R.color.ShopHop_textColorPrimary))
        menuWishItem.actionView.onClick {
            activity!!.launchActivity<ShopHopMyCartActivity> { }
        }
        setCartCount()
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> {
                activity!!.launchActivity<ShopHopSearchActivity>()
                true
            }
            R.id.action_filter -> {
                if (filterFragment == null) {
                    filterFragment = ShopHopFilterFragment.newInstance(map, categoryList)
                    filterFragment!!.mListener = listener
                }
                filterFragment!!.show(activity!!.supportFragmentManager, "Filter")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun setCartCount() {
        val count = getCartCount()
        if (menuCart != null) {
            menuCart!!.tvNotificationCount.text = count
            if (count.checkIsEmpty() || count == "0") {
                menuCart!!.tvNotificationCount.hide()
            } else {
                menuCart!!.tvNotificationCount.show()
            }
        }

    }

    private fun listAllProducts(mId: Int) {
        listAllProducts {
            mListAdapter.clearData()
            mGridAdapter.clearData()
            list.clear()

            when (mId) {
                FEATURED -> {
                    it.forEach { model ->
                        if (model.featured) {
                            list.add(model)
                            mListAdapter.addNewItem(model)
                            mGridAdapter.addNewItem(model)
                        }
                    }
                }
                NEWEST -> {
                    list.addAll(it)
                    mListAdapter.addItems(it)
                    mGridAdapter.addItems(it)
                }
            }
            getFilterAttributes()
        }
    }

    private fun getSubCategoryProducts(b: Boolean) {
        subCategoryProducts(mCategoryId!!) {
            list.clear()

            if (b) {
                mListAdapter.clearData()
                mGridAdapter.clearData()
                it.forEach { model ->
                    if (model.featured) {
                        list.add(model)
                        mListAdapter.addNewItem(model)
                        mGridAdapter.addNewItem(model)
                    }
                }
            } else {
                list.addAll(it)
                mListAdapter.addItems(it)
                mGridAdapter.addItems(it)
            }
            getFilterAttributes()
        }

    }

    private fun getRecentItems(): ArrayList<ProductModel> {
        val list = recentProduct()
        list.reverse()
        return list
    }

    var map = HashMap<String, ArrayList<Attribute>>()
    var categoryList = ArrayList<CategoryData>()

    fun getFilterAttributes() {
        categoryFromAssets(onApiSuccess = {
            categoryList.addAll(it)
            map = attributes()
            if (filterFragment != null) {
                filterFragment!!.invalidate(map, categoryList)
            }
            hideProgress()
            Log.e("attributes", map.size.toString() + "****" + Gson().toJson(map))
        })
    }

}