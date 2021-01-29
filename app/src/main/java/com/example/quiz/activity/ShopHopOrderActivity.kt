package com.example.quiz.activity

import android.os.Bundle
import android.view.View
import com.example.quiz.ShopHopAppBaseActivity
import com.example.quiz.R
import com.example.quiz.base.BaseRecyclerAdapter
import com.example.quiz.databinding.ShophopItemOrderlistBinding
import com.example.quiz.models.MyOrderData
import com.example.quiz.utils.ShopHopConstants
import com.example.quiz.utils.ShopHopConstants.KeyIntent.DATA
import com.example.quiz.utils.extensions.*
import kotlinx.android.synthetic.main.shophop_activity_order.*
import kotlinx.android.synthetic.main.shophop_layout_nodata.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlin.math.roundToInt


class ShopHopOrderActivity : ShopHopAppBaseActivity() {

    private lateinit var mOrderAdapter: BaseRecyclerAdapter<MyOrderData, ShophopItemOrderlistBinding>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shophop_activity_order)

        title = getString(R.string.sh_title_my_orders)
        setToolbar(toolbar)

        disableHardwareRendering(rvOrder)
        mOrderAdapter = object : BaseRecyclerAdapter<MyOrderData, ShophopItemOrderlistBinding>() {
            override fun onItemLongClick(view: View, model: MyOrderData, position: Int) {

            }

            override fun onItemClick(view: View, model: MyOrderData, position: Int, dataBinding: ShophopItemOrderlistBinding) {
                if (view.id == R.id.rlMainOrder) {
                    launchActivity<ShopHopOrderDescriptionActivity> {
                        putExtra(DATA, model)
                    }
                }
            }

            override val layoutResId: Int = R.layout.shophop_item_orderlist

            override fun onBindData(model: MyOrderData, position: Int, dataBinding: ShophopItemOrderlistBinding) {
                dataBinding.tvOriginalPrice.applyStrike()

                if (model.line_items.size > 0) {
                    dataBinding.tvProductName.text = model.line_items[0].name
                    if (model.line_items[0].image.isNotEmpty()) {
                        dataBinding.ivProduct.loadImageFromUrl(model.line_items[0].image)
                    }
                } else {
                    dataBinding.tvProductName.text = getString(R.string.sh_hint_no_products)
                }
                dataBinding.tvPrice.text = (model.total.toFloat() - model.discount_total.toFloat()).roundToInt().toString().currencyFormat(model.currency)
                if (model.discount_total == 0.0) {
                    dataBinding.tvOriginalPrice.hide()
                } else {
                    dataBinding.tvOriginalPrice.show()
                    dataBinding.tvOriginalPrice.text = model.total.toFloat().roundToInt().toString().currencyFormat(model.currency)
                }
                dataBinding.ivCircle.setCircleColor(color(R.color.ShopHop_track_yellow))

                when (model.status) {
                    ShopHopConstants.OrderStatus.PENDING -> {
                        dataBinding.tvTrack1.text = (toDate(model.date_created) + "<br/>Order <font color=#ECC134>Pending</font>").getHtmlString()
                        dataBinding.tvTrack2.text = getString(R.string.sh_lbl_order_pend).getHtmlString()
                    }
                    ShopHopConstants.OrderStatus.PROCESSING -> {
                        dataBinding.tvTrack1.text = (toDate(model.date_created) + "<br/>Order <font color=#64B931>Processing</font>").getHtmlString()
                        dataBinding.tvTrack2.text = getString(R.string.sh_lbl_item_delivering).getHtmlString()
                        dataBinding.ivCircle.setCircleColor(color(R.color.ShopHop_track_green))
                    }
                    ShopHopConstants.OrderStatus.ONHOLD -> {
                        dataBinding.tvTrack1.text = (toDate(model.date_created) + "<br/>Order <font color=#ECC134>On Hold</font>").getHtmlString()
                        dataBinding.tvTrack2.text = getString(R.string.sh_lbl_order_hold).getHtmlString()
                    }
                    ShopHopConstants.OrderStatus.COMPLETED -> {
                        dataBinding.tvTrack1.text = (toDate(model.date_created) + "<br/>Order <font color=#64B931>Placed</font>").getHtmlString()
                        dataBinding.tvTrack2.text = getString(R.string.sh_lbl_order_completed).getHtmlString()
                        dataBinding.tvProductDeliveryDate.text = toDate(model.date_completed!!)
                        dataBinding.ivCircle.setCircleColor(color(R.color.ShopHop_track_green))
                        dataBinding.ivLine.setLineColor(color(R.color.ShopHop_track_green))
                        dataBinding.ivCircle1.setCircleColor(color(R.color.ShopHop_track_green))
                    }
                    ShopHopConstants.OrderStatus.CANCELLED -> {
                        dataBinding.ivCircle.setCircleColor(color(R.color.ShopHop_track_red))
                        dataBinding.tvTrack1.text = (toDate(model.date_created) + "<br/>Order <font color=#F61929>Cancelled</font>").getHtmlString()
                        dataBinding.tvTrack2.text = getString(R.string.sh_lbl_order_cacelled).getHtmlString()
                    }
                    ShopHopConstants.OrderStatus.REFUNDED -> {
                        dataBinding.ivCircle.setCircleColor(color(R.color.ShopHop_track_grey))
                        dataBinding.tvTrack1.text = (toDate(model.date_created) + "<br/>Order <font color=#D3D3D3>Refunded</font>").getHtmlString()
                        dataBinding.tvTrack2.text = getString(R.string.sh_lbl_refunded).getHtmlString()
                    }
                    else -> {
                        dataBinding.ivCircle.setCircleColor(color(R.color.ShopHop_track_red))
                        dataBinding.tvTrack1.text = (toDate(model.date_created) + "<br/>Order <font color=#F61929>Trashed</font>").getHtmlString()
                        dataBinding.tvTrack2.text = "Order Trashed"
                    }
                }

                if (model.status == ShopHopConstants.OrderStatus.COMPLETED) {
                    dataBinding.llDeliveryDate.show()
                    dataBinding.llDeliveryInfo.show()
                    dataBinding.rlStatus.hide()
                    dataBinding.tvProductDeliveryDate.text = toDate(model.date_completed!!)
                } else {
                    dataBinding.llDeliveryDate.hide()
                    dataBinding.llDeliveryInfo.hide()
                    dataBinding.rlStatus.show()
                }
            }
        }
        rvOrder.adapter = mOrderAdapter

        val list = getOrders()
        if (list.size == 0) {
            rlNoData.show()
        } else {
            rlNoData.hide()
            mOrderAdapter.addItems(list)
        }
//        loadBannerAd(R.id.adView)

    }

}
