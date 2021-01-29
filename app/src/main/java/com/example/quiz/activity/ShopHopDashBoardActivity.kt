package com.example.quiz.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.quiz.ShopHopAppBaseActivity
import com.example.quiz.R
import com.example.quiz.fragments.*
import com.example.quiz.models.CategoryData
import com.example.quiz.utils.ShopHopConstants
import com.example.quiz.utils.ShopHopConstants.AppBroadcasts.CARTITEM_UPDATE
import com.example.quiz.utils.ShopHopConstants.AppBroadcasts.CART_COUNT_CHANGE
import com.example.quiz.utils.ShopHopConstants.AppBroadcasts.ORDER_COUNT_CHANGE
import com.example.quiz.utils.ShopHopConstants.AppBroadcasts.PROFILE_UPDATE
import com.example.quiz.utils.ShopHopConstants.AppBroadcasts.WISHLIST_UPDATE
import com.example.quiz.utils.ShopHopConstants.SharedPref.KEY_ORDER_COUNT
import com.example.quiz.utils.extensions.*
import kotlinx.android.synthetic.main.shophop_activity_dashboard_shop.*
import kotlinx.android.synthetic.main.shophop_bottom_bar.*
import kotlinx.android.synthetic.main.shophop_item_navigation_category.view.*
import kotlinx.android.synthetic.main.shophop_layout_sidebar.*
import kotlinx.android.synthetic.main.shophop_menu_cart.*
import kotlinx.android.synthetic.main.toolbar.*

class ShopHopDashBoardActivity : ShopHopAppBaseActivity() {

    private var count: String = ""
    private val mHomeFragment = ShopHopHomeFragment()
    private val mWishListFragment = ShopHopWishListFragment()
    private val mCartFragment = ShopHopMyCartFragment()
    private val mProfileFragment = ShopHopProfileFragment()
    private val mViewAllProductFragment = ShopHopViewAllProductFragment()
    private val mSearchFragment = ShopHopSearchFragment()
    private var selectedFragment: Fragment? = null

    private val mCartItemChangedReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                CART_COUNT_CHANGE -> setCartCountFromPref()
                ORDER_COUNT_CHANGE -> setOrderCount()
                PROFILE_UPDATE -> setUserInfo()
                WISHLIST_UPDATE -> setWishCount()
                CARTITEM_UPDATE -> {
                    mCartFragment.setCart()
                }
            }
        }
    }

    private fun setOrderCount() {
        tvOrderCount.text =
                getSharedPrefInstance().getIntValue(KEY_ORDER_COUNT, 0).toDecimalFormat()
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shophop_activity_dashboard_shop)

        registerCartCountChangeReceiver(mCartItemChangedReceiver)
        registerOrderCountChangeReceiver(mCartItemChangedReceiver)
        registerProfileUpdateReceiver(mCartItemChangedReceiver)
        registerWishListReceiver(mCartItemChangedReceiver)
        registerCartReceiver(mCartItemChangedReceiver)

        setToolbar(toolbar)
        setUpDrawerToggle()
        setListener()

        if (isLoggedIn()) {
            cartCount()
            setOrderCount()
            setWishCount()
            setCartCountFromPref()
            llInfo.show()

        }

        ivCloseDrawer.onClick {
            closeDrawer()
        }


        setUserInfo()
        tvVersionCode.text = "v " + getAppVersionName()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == ShopHopConstants.RequestCode.ACCOUNT) {
            loadWishlistFragment()
        }
    }


    private fun setUserInfo() {
        txtDisplayName.text = getUserFullName()
        if (getProfile().isNotEmpty() && isLoggedIn()) {
            val uri = Uri.parse(getProfile())
            if (uri != null) civProfile.setImageURI(uri)
        }
    }

    private fun setWishCount() {
        tvWishListCount.text =
                getSharedPrefInstance().getIntValue(ShopHopConstants.SharedPref.KEY_WISHLIST_COUNT, 0).toDecimalFormat()
        mWishListFragment.wishListItemChange()
    }


    private fun closeDrawer() {
        if (drawerLayout1.isDrawerOpen(llLeftDrawer))
            drawerLayout1.closeDrawer(llLeftDrawer)
    }

    private fun setUpDrawerToggle() {
        val toggleshophop = object : ActionBarDrawerToggle(this, drawerLayout1, toolbar, R.string.sh_navigation_drawer_open, R.string.sh_navigation_drawer_close) {

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                super.onDrawerSlide(drawerView, slideOffset)
                main1.translationX = slideOffset * drawerView.width
                (drawerLayout1).bringChildToFront(drawerView)
                (drawerLayout1).requestLayout()
            }
        }
        toggleshophop.setToolbarNavigationClickListener {
            if (!mSearchFragment.isVisible) {
                if (drawerLayout1.isDrawerVisible(GravityCompat.START)) {
                    drawerLayout1.closeDrawer(GravityCompat.START)
                } else {
                    drawerLayout1.openDrawer(GravityCompat.START)
                }
            }
        }
        toggleshophop.isDrawerIndicatorEnabled = false
        val drawable = ResourcesCompat.getDrawable(resources, R.drawable.shophop_ic_drawer, theme)
        toggleshophop.setHomeAsUpIndicator(drawable)
        drawerLayout1.addDrawerListener(toggleshophop)
        toggleshophop.syncState()
    }

    private fun setListener() {
        loadHomeFragment()
        civProfile.onClick {
            if (isLoggedIn()) {
                launchActivity<ShopHopEditProfileActivity>()
            } else {
                launchActivity<ShopHopSignInUpActivity>()
            }
        }
        llHome.onClick {
            closeDrawer()
            enable(ivHome)
            loadFragment(mHomeFragment)
            title = getString(R.string.sh_home)
        }

        llWishList.onClick {
            if (!isLoggedIn()) {
                launchActivity<ShopHopSignInUpActivity>()
                return@onClick
            }
            closeDrawer()
            enable(ivWishList)
            loadFragment(mWishListFragment)
            title = getString(R.string.sh_lbl_wish_list)
        }
        llWishlistData.onClick {
            if (!isLoggedIn()) {
                launchActivity<ShopHopSignInUpActivity>()
                return@onClick
            }
            closeDrawer()
            loadWishlistFragment()
        }

        llCart.onClick {
            if (!isLoggedIn()) {
                launchActivity<ShopHopSignInUpActivity>()
                return@onClick
            }
            closeDrawer()
            enable(ivCart)
            tvNotificationCount.hide()
            loadFragment(mCartFragment)
            title = getString(R.string.sh_cart)
        }

        llProfile.onClick {
            if (!isLoggedIn()) {
                launchActivity<ShopHopSignInUpActivity>()
                return@onClick
            }
            closeDrawer()
            enable(ivProfile)
            loadFragment(mProfileFragment)
            title = getString(R.string.sh_profile)

        }

        tvAccount.onClick {
            if (!isLoggedIn()) {
                launchActivity<ShopHopSignInUpActivity>()
            } else {
                launchActivity<ShopHopAccountActivity>(ShopHopConstants.RequestCode.ACCOUNT)
            }
            closeDrawer()

        }
        llReward.onClick {
            if (isLoggedIn()) {
                launchActivity<ShopHopRewardActivity>()
            } else {
                launchActivity<ShopHopSignInUpActivity>()
            }
            closeDrawer()

        }
        tvHelp.onClick {
            launchActivity<ShopHopHelpActivity>()
            closeDrawer()

        }
        tvSetting.onClick {
            launchActivity<ShopHopSettingActivity>()
            closeDrawer()

        }
        tvFaq.onClick {
            launchActivity<ShopHopFAQActivity>()
            closeDrawer()
        }
        tvContactus.onClick {
            launchActivity<ShopHopContactUsActivity>()
            closeDrawer()
        }
        ivCloseDrawer.onClick {
            closeDrawer()
        }
        llOrders.onClick {
            if (isLoggedIn()) {
                launchActivity<ShopHopOrderActivity>()
            } else {
                launchActivity<ShopHopSignInUpActivity>()
            }
            closeDrawer()
        }
    }

    private fun showCartCount() {
        if (isLoggedIn() && !count.checkIsEmpty() && !count.equals("0", false)) {
            tvNotificationCount.show()
        } else {
            tvNotificationCount.hide()
        }

    }

    private fun loadFragment(aFragment: Fragment) {
        when {
            mSearchFragment.isVisible -> {
                removeFragment(mSearchFragment)
            }
        }
        if (selectedFragment != null) {
            if (selectedFragment == aFragment) {
                return
            }
            hideFragment(selectedFragment!!)
        }
        if (aFragment.isAdded) {
            showFragment(aFragment)
        } else {
            addFragment(aFragment, R.id.container)
        }
        selectedFragment = aFragment
    }

    fun loadHomeFragment() {
        enable(ivHome)
        loadFragment(mHomeFragment)
        title = getString(R.string.sh_home)
    }

    private fun loadWishlistFragment() {
        enable(ivWishList)
        loadFragment(mWishListFragment)
        title = getString(R.string.sh_lbl_wish_list)
    }

    private fun enable(aImageView: ImageView?) {
        disableAll()
        showCartCount()
        aImageView?.background = getDrawable(R.drawable.shophop_bg_circle_primary_light)
        aImageView?.applyColorFilter(color(R.color.ShopHop_colorPrimary))
    }

    private fun disableAll() {
        disable(ivHome)
        disable(ivWishList)
        disable(ivCart)
        disable(ivProfile)
    }

    private fun disable(aImageView: ImageView?) {
        aImageView?.background = null
        aImageView?.applyColorFilter(color(R.color.ShopHop_textColorSecondary))
    }

    override fun onBackPressed() {
        when {
            drawerLayout1.isDrawerOpen(GravityCompat.START) -> drawerLayout1.closeDrawer(GravityCompat.START)
            mViewAllProductFragment.isVisible -> loadHomeFragment()
            mSearchFragment.isVisible -> {
                removeFragment(mSearchFragment)
                loadHomeFragment()
            }
            !mHomeFragment.isVisible -> loadHomeFragment()
            else -> super.onBackPressed()
        }
    }


    private fun cartCount() {
        setCartCountFromPref()
        sendCartCountChangeBroadcast()
    }

    private fun setCartCountFromPref() {
        count = getCartCount()

        tvNotificationCount.text = count
        showCartCount()
        if (mCartFragment.isVisible) {
            tvNotificationCount.hide()
        }
    }


    override fun onDestroy() {
        unregisterReceiver(mCartItemChangedReceiver)
        super.onDestroy()
    }

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


    fun setDrawerCategory(it: ArrayList<CategoryData>) {
        rvCategory.create(it.size, R.layout.shophop_item_navigation_category, it, getVerticalLayout(false), onBindView = { item, position ->
            tvCategory.text = item.name
            ivCat.setImageResource(mDrawables[position])
        }, itemClick = { item, _ ->
            launchActivity<ShopHopSubCategoryActivity> {
                putExtra(ShopHopConstants.KeyIntent.DATA, item)
            }
        })
        rvCategory.isNestedScrollingEnabled = false
    }

}
