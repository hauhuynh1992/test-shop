package com.example.quiz.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.example.quiz.ShopHopAppBaseActivity
import com.example.quiz.R
import com.example.quiz.adapter.ShopHopProductImageAdapter
import com.example.quiz.adapter.ShopHopTabAdapter
import com.example.quiz.fragments.ShopHopItemDescriptionFragment
import com.example.quiz.fragments.ShopHopMoreInfoFragment
import com.example.quiz.fragments.ShopHopReviewFragment
import com.example.quiz.models.Key
import com.example.quiz.models.ProductModel
import com.example.quiz.models.ProductReviewData
import com.example.quiz.utils.ShopHopConstants.KeyIntent.DATA
import com.example.quiz.utils.ShopHopConstants.KeyIntent.IS_ADDED_TO_CART
import com.example.quiz.utils.extensions.*
import com.google.android.material.tabs.TabLayout
import com.example.quiz.databinding.ShophopActivityProductDetailBinding
import kotlinx.android.synthetic.main.shophop_activity_product_detail.*

class ShopHopProductDetailActivity : ShopHopAppBaseActivity() {

    private var isAddedTocart: Boolean = false
    private var mFlag: Int = -1
    private lateinit var mMainBinding: ShophopActivityProductDetailBinding
    private lateinit var mProductModel: ProductModel
    private val mImages = ArrayList<String>()
    private lateinit var itemDescription: ShopHopItemDescriptionFragment
    private lateinit var itemMoreInfoFragment: ShopHopMoreInfoFragment
    private lateinit var itemReviewFragment: ShopHopReviewFragment
    var i: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        makeTransparentStatusBar()
        if (intent.getSerializableExtra(DATA) == null) {
            toast(R.string.sh_error_something_went_wrong)
            finish()
            return
        }
        mMainBinding = DataBindingUtil.setContentView(this, R.layout.shophop_activity_product_detail)
        setToolbar(mMainBinding.toolbar)

        mProductModel = intent.getSerializableExtra(DATA) as ProductModel
        mMainBinding.model = mProductModel

        itemDescription = ShopHopItemDescriptionFragment.getNewInstance(mProductModel)
        itemMoreInfoFragment = ShopHopMoreInfoFragment.getNewInstance(mProductModel)
        itemReviewFragment = ShopHopReviewFragment.getNewInstance(mProductModel)

        listProductReviews()
        if (mProductModel.on_sale)
            tvPrice.text = mProductModel.sale_price.currencyFormat()
        else
            tvPrice.text = mProductModel.price.currencyFormat()
        tvItemProductOriginalPrice.text = mProductModel.regular_price.currencyFormat()

        toolbar_layout.setCollapsedTitleTypeface(fontSemiBold())
        toolbar_layout.setExpandedTitleTypeface(fontSemiBold())
        toolbar_layout.title = mProductModel.name

        intHeaderView()

        tvItemProductOriginalPrice.applyStrike()
        setCustomFont()
        if (isLoggedIn()) {
            mFlag = isFavourite(mProductModel.id)
            if (mFlag != -1) {
                changeFavIcon(R.drawable.shophop_ic_heart_fill, R.color.ShopHop_favourite_background, R.color.ShopHop_colorPrimary)
            } else {
                changeFavIcon(R.drawable.shophop_ic_heart, R.color.ShopHop_favourite_unselected_background)
            }
        }
        ivFavourite.onClick {
            if (mFlag == -1) {
                if (isLoggedIn()) {
                    addToWishList(mProductModel, onApiSuccess = {
                        mFlag = it
                        changeFavIcon(
                                R.drawable.shophop_ic_heart_fill,
                                R.color.ShopHop_favourite_background,
                                R.color.ShopHop_colorPrimary
                        )
                    })
                }
            } else {
                removeWishList(mFlag, onApiSuccess = {
                    mFlag = -1
                    changeFavIcon(R.drawable.shophop_ic_heart, R.color.ShopHop_favourite_unselected_background)
                })
            }
        }
        isAddedTocart = intent.getBooleanExtra(IS_ADDED_TO_CART, false)
        if (!isAddedTocart) btnAddCard.text =
                getString(R.string.sh_lbl_add_to_cart) else btnAddCard.text =
                getString(R.string.sh_lbl_remove_cart)

        btnAddCard.onClick {
            if (isLoggedIn()) {
                if (isAddedTocart) removeCartItem() else addItemToCart(false)
            } else launchActivity<ShopHopSignInUpActivity> { }

        }
        btnBuyNow.onClick {
            if (isLoggedIn()) addItemToCart(true) else launchActivity<ShopHopSignInUpActivity> { }
        }
//        loadBannerAd(R.id.adView)

    }


    private fun intHeaderView() {
        for (i in 0 until mProductModel.images.size) {
            mImages.add(mProductModel.images[i].src)
        }
        val adapter1 = ShopHopProductImageAdapter(mImages)
        productViewPager.adapter = adapter1
        dots.attachViewPager(productViewPager)
        dots.setDotDrawable(R.drawable.shophop_bg_circle_primary, R.drawable.shophop_bg_black_dot)
        val array = arrayOf(itemDescription, itemMoreInfoFragment, itemReviewFragment)
        productTabs!!.addTab(productTabs!!.newTab().setText(getString(R.string.sh_lbl_tabDescription)))
        productTabs!!.addTab(productTabs!!.newTab().setText(getString(R.string.sh_lbl_tab_more_info)))
        productTabs!!.addTab(productTabs!!.newTab().setText(getString(R.string.sh_lbl_tabReviews)))
        val adapter = ShopHopTabAdapter(supportFragmentManager, array)
        ViewPagerInfo!!.adapter = adapter
        ViewPagerInfo!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(productTabs))
        productTabs!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                ViewPagerInfo!!.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        tvItemProductOriginalPrice.applyStrike()
        setCustomFont()
    }

    private fun changeFavIcon(
            drawable: Int,
            backgroundColor: Int,
            iconTint: Int = R.color.ShopHop_textColorSecondary
    ) {
        ivFavourite.setImageResource(drawable)
        ivFavourite.applyColorFilter(color(iconTint))
        ivFavourite.changeBackgroundTint(color(backgroundColor))
    }

    private fun setCustomFont() {
        val vg = productTabs.getChildAt(0) as ViewGroup
        val tabsCount = vg.childCount

        for (j in 0 until tabsCount) {
            val vgTab = vg.getChildAt(j) as ViewGroup

            val mTabChildCount = vgTab.childCount

            for (i in 0 until mTabChildCount) {
                val tabViewChild = vgTab.getChildAt(i)
                if (tabViewChild is TextView) {
                    tabViewChild.typeface = fontSemiBold()
                }
            }
        }
    }

    private fun addItemToCart(b: Boolean) {
        var mSelectedColors = ""
        var size = ""
        if (itemDescription.isColorAvailable()) {
            mSelectedColors = itemDescription.getSelectedColors()
            if (mSelectedColors.isEmpty()) {
                snackBar(getString(R.string.sh_msg_selectcolorsize))
                return
            }
        }
        if (itemDescription.isSizeAvailable()) {
            size = itemDescription.getSelectedSize()
            if (size.isEmpty()) {
                snackBar(getString(R.string.sh_msg_selectcolorsize))
                return
            }
        }
        addCart(getCartObject(mProductModel, mSelectedColors, size))
        btnAddCard.text = getString(R.string.sh_lbl_remove_cart)
        isAddedTocart = true
        if (b) {
            launchActivity<ShopHopMyCartActivity>()
        }

    }


    private fun removeCartItem() {
        getAlertDialog(getString(R.string.sh_msg_confirmation), onPositiveClick = { _, _ ->
            val key = Key()
            key.product_id = mProductModel.id
            removeItem(key)
            snackBar(getString(R.string.sh_success))
            btnAddCard.text = getString(R.string.sh_lbl_add_to_cart)
            isAddedTocart = false

        }, onNegativeClick = { dialog, _ ->
            dialog.dismiss()
        }).show()
    }

    @SuppressLint("SetTextI18n")
    fun listProductReviews() {
        getProductsReviews(mProductModel.id, onApiSuccess = {
            setReviews(it)
        }, userReviewed = {

        })

    }

    private fun setRating(data: List<ProductReviewData>) {
        var fiveStar = 0
        var fourStar = 0
        var threeStar = 0
        var twoStar = 0
        var oneStar = 0
        for (reviewModel in data) {
            when (reviewModel.rating) {
                5 -> fiveStar++
                4 -> fourStar++
                3 -> threeStar++
                2 -> twoStar++
                1 -> oneStar++
            }
        }
        val mAvgRating = (5 * fiveStar + 4 * fourStar + 3 * threeStar + 2 * twoStar + 1 * oneStar) / (fiveStar + fourStar + threeStar + twoStar + oneStar)
        tvItemProductRating.text = mAvgRating.toString()
    }

    fun setReviews(it: java.util.ArrayList<ProductReviewData>) {
        val mReview: ArrayList<String> = ArrayList()
        it.forEach { review ->
            if (!mReview.contains(review.email)) {
                mReview.add(review.email)
            }
        }
        setRating(it)
        tvItemProductReview.text = String.format("%d Reviewer", mReview.size)
    }


}
