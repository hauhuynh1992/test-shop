package com.example.quiz.activity

import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import com.example.quiz.ShopHopAppBaseActivity
import com.example.quiz.R
import com.example.quiz.base.BaseRecyclerAdapter
import com.example.quiz.databinding.ShophopItemProductBinding
import com.example.quiz.fragments.ShopHopSearchFragment
import com.example.quiz.fragments.ShopHopViewAllProductFragment
import com.example.quiz.models.CategoryData
import com.example.quiz.models.ProductModel
import com.example.quiz.utils.ShopHopConstants
import com.example.quiz.utils.ShopHopConstants.KeyIntent.DATA
import com.example.quiz.utils.extensions.*
import kotlinx.android.synthetic.main.shophop_activity_sub_category.*
import kotlinx.android.synthetic.main.shophop_activity_sub_category.viewPopular
import kotlinx.android.synthetic.main.toolbar.*


class ShopHopSubCategoryActivity : ShopHopAppBaseActivity() {
    private var imgLayoutParams: LinearLayout.LayoutParams? = null
    private lateinit var mCategoryData: CategoryData
    private var mFeatured: ArrayList<ProductModel> = ArrayList()
    private val mImg = ArrayList<String>()
    private var mNewArrivalAdapter = getAdapter()
    private var mPopularAdapter = getFeaturedAdapter()
    private var mViewAllProductFragment = ShopHopViewAllProductFragment()
    private var mSearchFragment = ShopHopSearchFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shophop_activity_sub_category)
//        loadBannerAd(R.id.adView)

        if (intent.getSerializableExtra(DATA) == null) {
            toast(R.string.sh_error_something_went_wrong)
            finish()
            return
        }
        mCategoryData = intent.getSerializableExtra(DATA) as CategoryData

        setToolbar(toolbar)
        title = mCategoryData.name
        imgLayoutParams = productLayoutParams()
        rcvNewArrival.setHorizontalLayout()
        rcvPopular.setHorizontalLayout()
        rcvNewArrival.adapter = mNewArrivalAdapter
        rcvPopular.adapter = mPopularAdapter
        mNewArrivalAdapter.setModelSize(5)
        mPopularAdapter.setModelSize(5)

        viewNewArrival.onClick {
            launchActivity<ShopHopViewAllProductActivity> {
                putExtra(ShopHopConstants.KeyIntent.TITLE, getString(R.string.sh_lbl_new_arrival))
                putExtra(ShopHopConstants.KeyIntent.VIEWALLID, ShopHopConstants.viewAllCode.CATEGORY_NEWEST)
                putExtra(ShopHopConstants.KeyIntent.KEYID, mCategoryData.name)

            }
        }
        viewPopular.onClick {
            launchActivity<ShopHopViewAllProductActivity> {
                putExtra(ShopHopConstants.KeyIntent.TITLE, getString(R.string.sh_lbl_popular))
                putExtra(ShopHopConstants.KeyIntent.VIEWALLID, ShopHopConstants.viewAllCode.CATEGORY_FEATURED)
                putExtra(ShopHopConstants.KeyIntent.KEYID, mCategoryData.name)
            }

        }
        getSubCategoryProducts()


    }

    private fun getSubCategoryProducts() {
        subCategoryProducts(mCategoryData.name) {
            mNewArrivalAdapter.addItems(it)
            it.forEach { model ->
                if (model.featured) {
                    mFeatured.add(model)
                }
            }
            if (mFeatured.size == 0) {
                rlFeature.hide()
                rcvPopular.hide()
            } else {
                mPopularAdapter.addItems(mFeatured)
            }

            it.forEach {
                if (it.images.isNotEmpty()) {
                    mImg.add(it.images[0].src)
                }
            }

            if (mImg.size > 0) {
                val handler = Handler()
                val runnable = object : Runnable {
                    var i = 0

                    override fun run() {
                        ivOffer1.loadImageFromUrl(mImg[i])
                        i++
                        if (i > mImg.size - 1) {
                            i = 0
                        }
                        handler.postDelayed(this, 3000)
                    }
                }
                handler.postDelayed(runnable, 3000)
            }


            showProgress(false)
            rlContent.show()
        }
    }



    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu!!.getItem(0)!!.isVisible = !mViewAllProductFragment.isVisible
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.shophop_menu_dashboard, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_search) {
            loadSearchFragment()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getAdapter(): BaseRecyclerAdapter<ProductModel, ShophopItemProductBinding> {

        return object : BaseRecyclerAdapter<ProductModel, ShophopItemProductBinding>() {
            override fun onItemClick(
                    view: View,
                    model: ProductModel,
                    position: Int,
                    dataBinding: ShophopItemProductBinding
            ) {
                if (view.id == R.id.rlItem) {
                    showProductDetail(model)
                }
            }

            override val layoutResId: Int = R.layout.shophop_item_product

            override fun onBindData(
                    model: ProductModel,
                    position: Int,
                    dataBinding: ShophopItemProductBinding
            ) {
                dataBinding.ivProduct.layoutParams = imgLayoutParams
                dataBinding.tvOriginalPrice.applyStrike()


                if (model.images.isNotEmpty()) {
                    dataBinding.ivProduct.loadImageFromUrl(model.images[0].src)
                }
                if (model.on_sale) {
                    dataBinding.tvDiscountPrice.text = model.sale_price.currencyFormat()

                } else {
                    dataBinding.tvDiscountPrice.text = model.price.currencyFormat()
                }
                dataBinding.tvOriginalPrice.applyStrike()
                dataBinding.tvOriginalPrice.text = model.regular_price.currencyFormat()
                //Glide.with(getAppInstance()).load(model.productImg!!).into(dataBinding.ivProduct)
            }

            override fun onItemLongClick(view: View, model: ProductModel, position: Int) {

            }
        }
    }

    private fun loadSearchFragment() {
        launchActivity<ShopHopSearchActivity>()
    }

    override fun onBackPressed() {
        when {
            mViewAllProductFragment.isVisible -> {
                title = intent.getStringExtra(ShopHopConstants.KeyIntent.TITLE)
                removeFragment(mViewAllProductFragment)
                invalidateOptionsMenu()
            }
            mSearchFragment.isVisible -> {
                onBackPressed()
            }
            else -> super.onBackPressed()

        }
    }

    private fun getFeaturedAdapter(): BaseRecyclerAdapter<ProductModel, ShophopItemProductBinding> {
        return object : BaseRecyclerAdapter<ProductModel, ShophopItemProductBinding>() {
            override fun onItemClick(
                    view: View,
                    model: ProductModel,
                    position: Int,
                    dataBinding: ShophopItemProductBinding
            ) {
                if (view.id == R.id.rlItem) {
                    showProductDetail(model)
                }
            }

            override val layoutResId: Int = R.layout.shophop_item_product

            override fun onBindData(
                    model: ProductModel,
                    position: Int,
                    dataBinding: ShophopItemProductBinding
            ) {
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


}