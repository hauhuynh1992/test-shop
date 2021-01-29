package com.example.quiz.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.quiz.ShopHopAppBaseActivity
import com.example.quiz.R
import com.example.quiz.base.ShopHopExpandableListAdapter
import com.example.quiz.databinding.ShophopItemFaqHeadingBinding
import com.example.quiz.databinding.ShophopItemFaqSubheadingBinding
import com.example.quiz.models.Category
import com.example.quiz.models.SubCategory
import com.example.quiz.utils.extensions.*
import kotlinx.android.synthetic.main.shophop_activity_faqactrivity.*
import kotlinx.android.synthetic.main.shophop_menu_cart.view.*
import kotlinx.android.synthetic.main.toolbar.*

class ShopHopFAQActivity : ShopHopAppBaseActivity() {
    private lateinit var mMenuCart: View
    private lateinit var mFaqAdapter: ShopHopExpandableListAdapter<Category, SubCategory, ShophopItemFaqHeadingBinding, ShophopItemFaqSubheadingBinding>

    private val mCartCountChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            setCartCount()

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shophop_activity_faqactrivity)
        title = getString(R.string.sh_title_faq)
        setToolbar(toolbar)
        registerCartCountChangeReceiver(mCartCountChangeReceiver)
        setFaq()
//        loadBannerAd(R.id.adView)

    }

    private fun setFaq() {
        mFaqAdapter = object : ShopHopExpandableListAdapter<Category, SubCategory, ShophopItemFaqHeadingBinding, ShophopItemFaqSubheadingBinding>(this) {

            override fun bindChildView(view: ShophopItemFaqSubheadingBinding, childObject: SubCategory, groupPosition: Int, childPosition: Int): ShophopItemFaqSubheadingBinding {
                return view
            }

            override fun bindGroupView(view: ShophopItemFaqHeadingBinding, groupObject: Category, groupPosition: Int): ShophopItemFaqHeadingBinding {
                return view
            }

            override val childItemResId: Int = R.layout.shophop_item_faq_subheading

            override val groupItemResId: Int = R.layout.shophop_item_faq_heading
        }
        exFaq.setAdapter(mFaqAdapter)
        addItems()
    }

    private fun addItems() {
        val mHeading = arrayOf(
                getString(R.string.sh_lbl_account_deactivate),
                getString(R.string.sh_lbl_quick_pay),
                getString(R.string.sh_lbl_return_items),
                getString(R.string.sh_lbl_replace_items)

        )
        val mSubHeading = arrayOf(
                getString(R.string.sh_lbl_account_deactivate),
                getString(R.string.sh_lbl_quick_pay),
                getString(R.string.sh_lbl_return_items),
                getString(R.string.sh_lbl_replace_items)
        )
        val map = HashMap<Category, ArrayList<SubCategory>>()
        val mFaqList = ArrayList<Category>()
        mHeading.forEachIndexed { _: Int, s: String ->
            val heading = Category()
            heading.categoryName = s
            mFaqList.add(heading)
        }
        mFaqList.forEach {
            val list = ArrayList<SubCategory>()
            mSubHeading.forEach {
                val subCat = SubCategory()
                subCat.categoryName = it
                list.add(subCat)
            }
            map[it] = list
        }
        mFaqAdapter.addExpandableItems(mFaqList, map)
//        setExpandableListViewHeight(exFaq, -1)
//        exFaq.setOnGroupClickListener { parent, v, position, id ->
//            setExpandableListViewHeight(parent, position)
//            false
//        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.shophop_menu_dashboard, menu)
        val menuWishItem: MenuItem = menu!!.findItem(R.id.action_cart)
        val menuSearch: MenuItem = menu.findItem(R.id.action_search)
        menuWishItem.isVisible = true
        menuSearch.isVisible = false
        mMenuCart = menuWishItem.actionView
        mMenuCart.ivCart.setColorFilter(this.color(R.color.ShopHop_textColorPrimary))
        setCartCount()
        menuWishItem.actionView.onClick {
            launchActivity<ShopHopMyCartActivity> { }
        }
        return super.onCreateOptionsMenu(menu)
    }

    fun setCartCount() {
        val count = getCartCount()
        mMenuCart.tvNotificationCount.text = count
        if (count.checkIsEmpty() || count=="0") {
            mMenuCart.tvNotificationCount.hide()
        } else {
            mMenuCart.tvNotificationCount.show()
        }
    }

    override fun onDestroy() {
        unregisterReceiver(mCartCountChangeReceiver)
        super.onDestroy()
    }
}
