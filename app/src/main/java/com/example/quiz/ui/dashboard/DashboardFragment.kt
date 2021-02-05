package com.example.quiz.ui.dashboard

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.quiz.R
import com.example.quiz.activity.*
import com.example.quiz.databinding.FragmentDashboardBinding
import com.example.quiz.databinding.FragmentSplashBinding
import com.example.quiz.fragments.*
import com.example.quiz.ui.cart.CartFragment
import com.example.quiz.ui.category.CategoryFragment
import com.example.quiz.ui.home.HomeFragment
import com.example.quiz.ui.profile.ProfileFragment
import com.example.quiz.ui.search.SearchFragment
import com.example.quiz.ui.wish.WishFragment
import com.example.quiz.utils.ShopHopConstants
import com.example.quiz.utils.extensions.*
import kotlinx.android.synthetic.main.shophop_activity_dashboard_shop.*
import kotlinx.android.synthetic.main.shophop_bottom_bar.*
import kotlinx.android.synthetic.main.shophop_layout_sidebar.*
import kotlinx.android.synthetic.main.shophop_menu_cart.*
import kotlinx.android.synthetic.main.toolbar.*

class DashboardFragment : Fragment() {

    private var count: String = ""
    private val mHomeFragment = HomeFragment()
    private val mWishListFragment = WishFragment()
    private val mCartFragment = CartFragment()
    private val mProfileFragment = ProfileFragment()
    private val mViewAllProductFragment = CategoryFragment()
    private val mSearchFragment = SearchFragment()
    private var selectedFragment: Fragment? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentDashboardBinding>(
            inflater,
            R.layout.fragment_dashboard, container, false
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setUpDrawerToggle()
        setListener()
//
//        if (isLoggedIn()) {
//            cartCount()
//            setOrderCount()
//            setWishCount()
//            setCartCountFromPref()
//            llInfo.show()
//
//        }

        ivCloseDrawer.onClick {
            closeDrawer()
        }


        setUserInfo()
        tvVersionCode.text = "v " + requireContext().getAppVersionName()

    }

    private fun setListener() {
        loadHomeFragment()
        civProfile.onClick {
            if (isLoggedIn()) {
//                launchActivity<ShopHopEditProfileActivity>()
            } else {
//                launchActivity<ShopHopSignInUpActivity>()
            }
        }
        llHome.onClick {
            closeDrawer()
            enable(ivHome)
            loadFragment(mHomeFragment)
//            title = getString(R.string.sh_home)
        }

        llWishList.onClick {
//            if (!isLoggedIn()) {
//                launchActivity<ShopHopSignInUpActivity>()
//                return@onClick
//            }
            closeDrawer()
            enable(ivWishList)
            loadFragment(mWishListFragment)
//            title = getString(R.string.sh_lbl_wish_list)
        }
        llWishlistData.onClick {
//            if (!isLoggedIn()) {
//                launchActivity<ShopHopSignInUpActivity>()
//                return@onClick
//            }
            closeDrawer()
            loadWishlistFragment()
        }

        llCart.onClick {
//            if (!isLoggedIn()) {
//                launchActivity<ShopHopSignInUpActivity>()
//                return@onClick
//            }
            closeDrawer()
            enable(ivCart)
            tvNotificationCount.hide()
            loadFragment(mCartFragment)
//            title = getString(R.string.sh_cart)
        }

        llProfile.onClick {
//            if (!isLoggedIn()) {
//                launchActivity<ShopHopSignInUpActivity>()
//                return@onClick
//            }
            closeDrawer()
            enable(ivProfile)
            loadFragment(mProfileFragment)
//            title = getString(R.string.sh_profile)

        }

        tvAccount.onClick {
//            if (!isLoggedIn()) {
//                launchActivity<ShopHopSignInUpActivity>()
//            } else {
//                launchActivity<ShopHopAccountActivity>(ShopHopConstants.RequestCode.ACCOUNT)
//            }
            closeDrawer()

        }
        llReward.onClick {
//            if (isLoggedIn()) {
//                launchActivity<ShopHopRewardActivity>()
//            } else {
//                launchActivity<ShopHopSignInUpActivity>()
//            }
            closeDrawer()

        }
        tvHelp.onClick {
//            launchActivity<ShopHopHelpActivity>()
            closeDrawer()

        }
        tvSetting.onClick {
//            launchActivity<ShopHopSettingActivity>()
            closeDrawer()

        }
        tvFaq.onClick {
//            launchActivity<ShopHopFAQActivity>()
            closeDrawer()
        }
        tvContactus.onClick {
//            launchActivity<ShopHopContactUsActivity>()
            closeDrawer()
        }
        ivCloseDrawer.onClick {
            closeDrawer()
        }
        llOrders.onClick {
//            if (isLoggedIn()) {
//                launchActivity<ShopHopOrderActivity>()
//            } else {
//                launchActivity<ShopHopSignInUpActivity>()
//            }
            closeDrawer()
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


    private fun closeDrawer() {
        if (drawerLayout1.isDrawerOpen(llLeftDrawer))
            drawerLayout1.closeDrawer(llLeftDrawer)
    }

    private fun enable(aImageView: ImageView?) {
        disableAll()
        showCartCount()
        aImageView?.background =
            resources.getDrawable(R.drawable.shophop_bg_circle_primary_light, null)
        aImageView?.applyColorFilter(resources.getColor(R.color.ShopHop_colorPrimary, null))
    }

    private fun showCartCount() {
//        if (isLoggedIn() && !count.checkIsEmpty() && !count.equals("0", false)) {
//            tvNotificationCount.show()
//        } else {
//            tvNotificationCount.hide()
//        }

    }

    private fun disableAll() {
        disable(ivHome)
        disable(ivWishList)
        disable(ivCart)
        disable(ivProfile)
    }

    private fun disable(aImageView: ImageView?) {
        aImageView?.background = null
        aImageView?.applyColorFilter(resources.getColor(R.color.ShopHop_textColorSecondary, null))
    }

    private fun loadWishlistFragment() {
        enable(ivWishList)
        loadFragment(mWishListFragment)
//        title = getString(R.string.sh_lbl_wish_list)
    }

    fun loadHomeFragment() {
        enable(ivHome)
        loadFragment(mHomeFragment)
//        title = getString(R.string.sh_home)
    }

    private fun cartCount() {
        setCartCountFromPref()
//        sendCartCountChangeBroadcast()
    }

    private fun setUpDrawerToggle() {
        val toggleshophop = object : ActionBarDrawerToggle(
            requireActivity(),
            drawerLayout1,
            toolbar,
            R.string.sh_navigation_drawer_open,
            R.string.sh_navigation_drawer_close
        ) {

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
        val drawable = ResourcesCompat.getDrawable(resources, R.drawable.shophop_ic_drawer, null)
        toggleshophop.setHomeAsUpIndicator(drawable)
        drawerLayout1.addDrawerListener(toggleshophop)
        toggleshophop.syncState()
    }

    private fun setCartCountFromPref() {
        count = getCartCount()

        tvNotificationCount.text = count
        showCartCount()
        if (mCartFragment.isVisible) {
            tvNotificationCount.hide()
        }
    }

    private fun setOrderCount() {
        tvOrderCount.text =
            getSharedPrefInstance().getIntValue(ShopHopConstants.SharedPref.KEY_ORDER_COUNT, 0)
                .toDecimalFormat()
    }

    private fun setWishCount() {
        tvWishListCount.text =
            getSharedPrefInstance().getIntValue(ShopHopConstants.SharedPref.KEY_WISHLIST_COUNT, 0)
                .toDecimalFormat()
//        mWishListFragment.wishListItemChange()
    }

    private fun setUserInfo() {
//        txtDisplayName.text = getUserFullName()
//        if (getProfile().isNotEmpty() && isLoggedIn()) {
//            val uri = Uri.parse(getProfile())
//            if (uri != null) civProfile.setImageURI(uri)
//        }
    }
}