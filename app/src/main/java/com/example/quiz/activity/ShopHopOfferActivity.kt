package com.example.quiz.activity


import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.quiz.ShopHopAppBaseActivity
import com.example.quiz.R
import com.example.quiz.base.BaseRecyclerAdapter
import com.example.quiz.databinding.ShophopItemOfferBinding
import com.example.quiz.models.ProductModel
import com.example.quiz.utils.ShopHopConstants
import com.example.quiz.utils.extensions.*
import kotlinx.android.synthetic.main.shophop_activity_offer.*
import kotlinx.android.synthetic.main.shophop_activity_offer.ivOffer1
import kotlinx.android.synthetic.main.shophop_menu_cart.view.*
import kotlinx.android.synthetic.main.toolbar.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ShopHopOfferActivity : ShopHopAppBaseActivity() {
    private val mGridAdapter = getGridAdapter()
    private var handler: Handler? = null
    private var runnable: Runnable? = null
    private lateinit var mMenuCart: View
    private val mImg = ArrayList<String>()

    private val mCartCountChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            setCartCount()
        }
    }

    private fun getGridAdapter(): BaseRecyclerAdapter<ProductModel, ShophopItemOfferBinding> {

        return object : BaseRecyclerAdapter<ProductModel, ShophopItemOfferBinding>() {

            override val layoutResId: Int = R.layout.shophop_item_offer

            override fun onBindData(model: ProductModel, position: Int, dataBinding: ShophopItemOfferBinding) {
                dataBinding.tvOffer.text=model.sale_price.currencyFormat()
                dataBinding.ivProduct.loadImageFromUrl(model.images[0].src)
            }

            override fun onItemClick(view: View, model:ProductModel, position:Int, dataBinding: ShophopItemOfferBinding) {
            }

            override fun onItemLongClick(view: View, model:ProductModel,position:Int) {}
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shophop_activity_offer)
        setToolbar(toolbar)
        title = getString(R.string.sh_title_offer)
        registerCartCountChangeReceiver(mCartCountChangeReceiver)
        rcvOffer.adapter = mGridAdapter
        rcvOffer.rvItemAnimation()
        getOffers()
        countDownStart()
//        loadBannerAd(R.id.adView)
        tvViewAll.onClick {
           launchActivity<ShopHopViewAllProductActivity> {
                putExtra(ShopHopConstants.KeyIntent.TITLE, "Offers")
                putExtra(ShopHopConstants.KeyIntent.VIEWALLID, ShopHopConstants.viewAllCode.OFFERS)
            }
        }

    }

    private fun getOffers() {
        listAllProducts {
            mGridAdapter.clearData()
            it.forEach {model->
                if (model.on_sale){
                    mGridAdapter.addNewItem(model)
                }
            }
            it.forEach {
                if (it.images.isNotEmpty()) {
                    mImg.add(it.images[0].src)
                }
            }
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
            showProgress(false)
            nsvContent.show()
        }
    }

    private fun countDownStart() {
        handler = Handler()
        runnable = object : Runnable {
            @SuppressLint("SetTextI18n", "SimpleDateFormat")
            override fun run() {
                handler!!.postDelayed(this, 1000)
                try {
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd")

                    val c = Calendar.getInstance()
                    c.add(Calendar.DATE, 1)
                    val a = dateFormat.format(c.time)
                    val futureDate = dateFormat.parse(a)
                    val currentDate = Date()
                    if (!currentDate.after(futureDate)) {
                        var diff = futureDate?.time!! - currentDate.time
                        val days = diff / (24 * 60 * 60 * 1000)
                        diff -= days * (24 * 60 * 60 * 1000)
                        val hours = diff / (60 * 60 * 1000)
                        diff -= hours * (60 * 60 * 1000)
                        val minutes = diff / (60 * 1000)
                        diff -= minutes * (60 * 1000)
                        txtDay.text = "" + String.format("%02d", days)
                        txtHour.text = "" + String.format("%02d", hours)
                        txtMinute.text = "" + String.format("%02d", minutes)
                        txtSecond.text = "" + String.format("%02d", diff / 1000)
                    } else {
                        textViewGone()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
        handler!!.postDelayed(runnable!!, 1 * 1000)
    }

    fun textViewGone() {
        LinearLayout1.visibility = View.GONE
        LinearLayout2.visibility = View.GONE
        LinearLayout3.visibility = View.GONE
        LinearLayout4.visibility = View.GONE

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
        if (count.checkIsEmpty()|| count=="0") {
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
