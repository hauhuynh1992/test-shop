package com.example.quiz.fragments

import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.quiz.ShopHopAppBaseActivity
import com.example.quiz.R
import com.example.quiz.activity.ShopHopDashBoardActivity
import com.example.quiz.activity.ShopHopSearchActivity
import com.example.quiz.activity.ShopHopSubCategoryActivity
import com.example.quiz.activity.ShopHopViewAllProductActivity
import com.example.quiz.adapter.ShopHopHomeSliderAdapter
import com.example.quiz.base.BaseRecyclerAdapter
import com.example.quiz.databinding.ShophopItemCategoryBinding
import com.example.quiz.databinding.ShophopItemHomeNewestProductBinding

import com.example.quiz.models.*
import com.example.quiz.utils.ShopHopConstants
import com.example.quiz.utils.extensions.*
import kotlinx.android.synthetic.main.shophop_fragment_home.*
import kotlinx.android.synthetic.main.shophop_fragment_home.view.*

class ShopHopHomeFragment : ShopHopBaseFragment() {
    private var mFeatured: ArrayList<ProductModel> = ArrayList()
    private var imgLayoutParams: LinearLayout.LayoutParams? = null
    private var mRecentProductAdapter: BaseRecyclerAdapter<ProductModel, ShophopItemHomeNewestProductBinding>? =
            null
    private var mPopularProductsAdapter: BaseRecyclerAdapter<ProductModel, ShophopItemHomeNewestProductBinding>? =
            null
    private var mNewestProductAdapter: BaseRecyclerAdapter<ProductModel, ShophopItemHomeNewestProductBinding>? =
            null
    private lateinit var mProductModel: ProductModel
    private val mImg = ArrayList<String>()
    var image: String = ""
    var size: Int? = 5
    private var mCategoryAdapter: BaseRecyclerAdapter<CategoryData, ShophopItemCategoryBinding>? = null
    private var mColorArray = intArrayOf(
            R.color.ShopHop_cat_1,
            R.color.ShopHop_cat_2,
            R.color.ShopHop_cat_3,
            R.color.ShopHop_cat_4,
            R.color.ShopHop_cat_5
    )
    private var mDrawables = intArrayOf(
            R.drawable.shophop_ic_men,
            R.drawable.shophop_ic_dress,
            R.drawable.shophop_ic_dress_kids,
            R.drawable.shophop_ic_bag,
            R.drawable.shophop_ic_watches,
            R.drawable.shophop_ic_candle,
            R.drawable.shophop_ic_glasses,
            R.drawable.shophop_ic_footwear,
            R.drawable.shophop_ic_sports
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        imgLayoutParams = activity?.productLayoutParams()


        setRecyclerView(view.rcvRecentSearch)
        setRecyclerView(rcvNewestProduct)
        setRecyclerView(rcvFeaturedProducts)
        setRecyclerView(rcvCategory)

        mRecentProductAdapter = getNewestProductAdapter()
        mPopularProductsAdapter = getFeaturedAdapter()
        mNewestProductAdapter = getNewestProductAdapter()
        mCategoryAdapter = getCategoryAdapter()

        rcvRecentSearch.adapter = mRecentProductAdapter
        rcvNewestProduct.adapter = mNewestProductAdapter
        rcvFeaturedProducts.adapter = mPopularProductsAdapter
        rcvCategory.adapter = mCategoryAdapter
        mRecentProductAdapter?.setModelSize(5)
        mPopularProductsAdapter?.setModelSize(5)
        setClickEventListener()
        listAllProducts()
        listAllProductCategories()
    }

    override fun onResume() {
        super.onResume()
        mRecentProductAdapter?.addItems(getRecentItems())


    }
    private fun getCategoryAdapter(): BaseRecyclerAdapter<CategoryData, ShophopItemCategoryBinding>? {
        return object : BaseRecyclerAdapter<CategoryData, ShophopItemCategoryBinding>() {
            override val layoutResId: Int = R.layout.shophop_item_category

            override fun onBindData(model: CategoryData, position: Int, dataBinding: ShophopItemCategoryBinding) {
                dataBinding.ivCategory.setImageResource(mDrawables[position])
                dataBinding.ivCategory.changeBackgroundTint(
                        ContextCompat.getColor(
                                context!!,
                                mColorArray[position % mColorArray.size]
                        )
                )
                dataBinding.tvCatName.setTextColor(activity!!.color(mColorArray[position % mColorArray.size]))
            }

            override fun onItemClick(view: View, model: CategoryData, position: Int, dataBinding: ShophopItemCategoryBinding) {
                activity?.launchActivity<ShopHopSubCategoryActivity> {
                    putExtra(ShopHopConstants.KeyIntent.DATA, model)
                }
            }

            override fun onItemLongClick(view: View, model: CategoryData, position: Int) {

            }

        }
    }

    private fun getRecentItems(): ArrayList<ProductModel> {
        val list = recentProduct()
        list.reverse()
        if (list.size <= 0) {
            rlRecentSearch.hide()
        } else {
            if (list.size<=5){
                viewRecentSearch.hide()
            }else{
                viewRecentSearch.show()
            }
            rlRecentSearch.show()
        }
        return list
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.shophop_fragment_home, container, false)
    }


    private fun listAllProducts() {
        listAllProducts {
            mNewestProductAdapter?.setModelSize(5)
            mNewestProductAdapter?.addItems(it)

            it.forEach { productModel ->
                if (productModel.featured) {
                    mFeatured.add(productModel)
                }
            }
            if (mFeatured.size == 0) {
                rlFeatured.hide()
                rcvFeaturedProducts.hide()
            } else {
                mPopularProductsAdapter?.addItems(mFeatured)
            }
            mNewestProductAdapter?.setModelSize(5)
            mPopularProductsAdapter?.setModelSize(5)

            for (i in 1 until 7) {
                mImg.add("/banners/b$i.jpg")
            }


            val adapter = ShopHopHomeSliderAdapter(mImg)
            homeSlider.adapter = adapter
            dots.attachViewPager(homeSlider)
            dots.setDotDrawable(R.drawable.shophop_bg_circle_primary, R.drawable.shophop_bg_black_dot)

        }
    }


    private fun setClickEventListener() {
        viewRecentSearch.onClick {
            activity?.launchActivity<ShopHopViewAllProductActivity> {
                putExtra(ShopHopConstants.KeyIntent.TITLE, "Recent Search")
                putExtra(ShopHopConstants.KeyIntent.VIEWALLID, ShopHopConstants.viewAllCode.RECENTSEARCH)
            }
        }

        viewPopular.onClick {
            activity?.launchActivity<ShopHopViewAllProductActivity> {
                putExtra(ShopHopConstants.KeyIntent.TITLE, "Featured Products")
                putExtra(ShopHopConstants.KeyIntent.VIEWALLID, ShopHopConstants.viewAllCode.FEATURED)
            }
        }

        viewNewest.onClick {
            activity?.launchActivity<ShopHopViewAllProductActivity> {
                putExtra(ShopHopConstants.KeyIntent.TITLE, "Newest Products")
                putExtra(ShopHopConstants.KeyIntent.VIEWALLID, ShopHopConstants.viewAllCode.NEWEST)
            }
        }
    }

    private fun setRecyclerView(recyclerView: RecyclerView?) {
        recyclerView?.setHorizontalLayout()
    }

    private fun getNewestProductAdapter(): BaseRecyclerAdapter<ProductModel, ShophopItemHomeNewestProductBinding>? {
        return object : BaseRecyclerAdapter<ProductModel, ShophopItemHomeNewestProductBinding>() {
            override fun onItemClick(view: View, model: ProductModel, position: Int, dataBinding: ShophopItemHomeNewestProductBinding) {
                (activity as ShopHopAppBaseActivity).showProductDetail(model)
               // mRecentProductAdapter?.addItems(getRecentItems())
               // rlRecentSearch.show()
            }

            override val layoutResId: Int = R.layout.shophop_item_home_newest_product

            override fun onBindData(model: ProductModel, position: Int, dataBinding: ShophopItemHomeNewestProductBinding) {
                if (model.images.isNotEmpty()) {
                    dataBinding.ivProduct.loadImageFromUrl(model.images[0].src)
                }
                dataBinding.ivProduct.layoutParams = imgLayoutParams
                if (model.on_sale) {
                    dataBinding.tvDiscountPrice.text = model.sale_price.currencyFormat()

                } else {
                    dataBinding.tvDiscountPrice.text = model.price.currencyFormat()
                }
                dataBinding.tvOriginalPrice.applyStrike()
                dataBinding.tvOriginalPrice.text = model.regular_price.currencyFormat()
            }

            override fun onItemLongClick(view: View, model: ProductModel, position: Int) {

            }
        }
    }

    private fun getFeaturedAdapter(): BaseRecyclerAdapter<ProductModel, ShophopItemHomeNewestProductBinding> {
        return object : BaseRecyclerAdapter<ProductModel, ShophopItemHomeNewestProductBinding>() {
            override fun onItemClick(view: View, model: ProductModel, position: Int, dataBinding: ShophopItemHomeNewestProductBinding) {
                (activity as ShopHopAppBaseActivity).showProductDetail(model)
               // mRecentProductAdapter?.addItems(getRecentItems())
                //rlRecentSearch.show()
            }

            override val layoutResId: Int = R.layout.shophop_item_home_newest_product

            override fun onBindData(model: ProductModel, position: Int, dataBinding: ShophopItemHomeNewestProductBinding) {
                mProductModel = model
                if (model.images.isNotEmpty()) {
                    dataBinding.ivProduct.loadImageFromUrl(model.images[0].src)
                }
                if (model.on_sale)
                    dataBinding.tvDiscountPrice.text = model.sale_price.currencyFormat()
                else
                    dataBinding.tvDiscountPrice.text = model.price.currencyFormat()
                dataBinding.ivProduct.layoutParams = imgLayoutParams
                dataBinding.tvOriginalPrice.applyStrike()
                dataBinding.tvOriginalPrice.text = model.regular_price.currencyFormat()
            }

            override fun onItemLongClick(view: View, model: ProductModel, position: Int) {

            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.shophop_menu_dashboard, menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> {
                activity?.launchActivity<ShopHopSearchActivity>()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun listAllProductCategories() {
        categoryFromAssets(onApiSuccess = {
            mCategoryAdapter?.addItems(it)
            rcvCategory.adapter = mCategoryAdapter
            (activity as ShopHopDashBoardActivity).setDrawerCategory(it)
        })

    }
}