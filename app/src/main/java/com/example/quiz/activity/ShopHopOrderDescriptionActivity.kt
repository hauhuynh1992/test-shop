package com.example.quiz.activity

import android.os.Bundle
import android.view.View
import com.example.quiz.ShopHopAppBaseActivity
import com.example.quiz.R
import com.example.quiz.base.BaseRecyclerAdapter
import com.example.quiz.databinding.ShophopItemOrderBinding
import com.example.quiz.models.LineItem
import com.example.quiz.models.MyOrderData
import com.example.quiz.utils.ShopHopConstants
import com.example.quiz.utils.ShopHopConstants.OrderStatus.CANCELLED
import com.example.quiz.utils.ShopHopConstants.OrderStatus.COMPLETED
import com.example.quiz.utils.ShopHopConstants.OrderStatus.ONHOLD
import com.example.quiz.utils.ShopHopConstants.OrderStatus.PENDING
import com.example.quiz.utils.ShopHopConstants.OrderStatus.PROCESSING
import com.example.quiz.utils.ShopHopConstants.OrderStatus.REFUNDED
import com.example.quiz.utils.extensions.*
import kotlinx.android.synthetic.main.shophop_activity_orderdescription.*
import kotlinx.android.synthetic.main.shophop_layout_paymentdetail.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlin.math.roundToInt

class ShopHopOrderDescriptionActivity : ShopHopAppBaseActivity() {

    private lateinit var mOrderItemAdapter: BaseRecyclerAdapter<LineItem, ShophopItemOrderBinding>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shophop_activity_orderdescription)
        title = getString(R.string.sh_title_my_orders)
        setToolbar(toolbar)
        disableHardwareRendering(llTrack)

        val orderModel = intent.getSerializableExtra(ShopHopConstants.KeyIntent.DATA) as MyOrderData
        mOrderItemAdapter = object : BaseRecyclerAdapter<LineItem, ShophopItemOrderBinding>() {
            override val layoutResId: Int = R.layout.shophop_item_order

            override fun onBindData(model: LineItem, position: Int, dataBinding: ShophopItemOrderBinding) {
                dataBinding.tvPrice.text =
                    model.total.roundToInt().toString().currencyFormat(orderModel.currency)
                dataBinding.tvOriginalPrice.text =
                    model.price.toString().currencyFormat(orderModel.currency)
                dataBinding.tvTotalItem.text =String.format("%s %d",getString(R.string.sh_text_total_item_1),model.quantity)
                dataBinding.tvOriginalPrice.applyStrike()
                if (model.color.isNotEmpty()) {
                    dataBinding.ivChecked.changeBackgroundTint(
                        android.graphics.Color.parseColor(
                            model.color
                        )
                    )
                }
                if (model.image.isNotEmpty()) {
                    dataBinding.ivProduct.loadImageFromUrl(model.image)
                }
            }

            override fun onItemClick(
                view: View,
                model: LineItem,
                position: Int,
                dataBinding: ShophopItemOrderBinding
            ) {
            }

            override fun onItemLongClick(view: View, model: LineItem, position: Int) {}

        }
        rvOrderItems.setVerticalLayout()
        rvOrderItems.adapter = mOrderItemAdapter
        bindOrderData(orderModel)

        llTrack.onClick {
            launchActivity<ShopHopTrackItemActivity> {
                putExtra(ShopHopConstants.KeyIntent.DATA, orderModel)
            }
        }
        ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            txtRatings.text = rating.toString()
        }
//        loadBannerAd(R.id.adView)

    }

    private fun bindOrderData(orderModel: MyOrderData) {
        mOrderItemAdapter.addItems(orderModel.line_items)
        val track1: String
        val track2: String
        ivCircle.setCircleColor(color(R.color.ShopHop_track_yellow))

        when (orderModel.status) {
            PENDING -> {
                track1 = "Order <font color=#ECC134>Pending</font>"
                track2 = "Order Pending"
            }
            PROCESSING -> {
                track1 = "Order <font color=#64B931>Processing</font>"
                track2 = "Item Delivering "
                ivCircle.setCircleColor(color(R.color.ShopHop_track_green))
            }
            ONHOLD -> {
                track1 = "Order <font color=#ECC134>On Hold</font>"
                track2 = "Order on hold"
            }
            COMPLETED -> {
                track1 = "Order <font color=#64B931>Placed</font>"
                track2 = "Order <font color=#64B931>Completed</font>"
                tvDeliveryDate.text = toDate(orderModel.date_completed!!)
                tvTrack2.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.shophop_ic_keyboard_arrow_right_black,
                    0
                )
                tvDeliveryDate.show()
                tvDelivered.show()
                ivCircle.setCircleColor(color(R.color.ShopHop_track_green))
                ivLine.setLineColor(color(R.color.ShopHop_track_green))
                ivCircle1.setCircleColor(color(R.color.ShopHop_track_green))
            }
            CANCELLED -> {
                ivCircle.setCircleColor(color(R.color.ShopHop_track_red))
                track1 = "Order <font color=#F61929>Cancelled</font>"
                track2 = "Order Cancelled"
            }
            REFUNDED -> {
                ivCircle.setCircleColor(color(R.color.ShopHop_track_grey))
                track1 = "Order <font color=#D3D3D3>Refunded</font>"
                track2 = "Order Refunded"
            }
            else -> {
                ivCircle.setCircleColor(color(R.color.ShopHop_track_red))
                track1 = "Order <font color=#F61929>Trashed</font>"
                track2 = "Order Trashed"
            }
        }

        tvDate.text = toDate(orderModel.date_created)
        tvTrack1.text = track1.getHtmlString()
        tvTrack2.text = track2.getHtmlString()
        //tvOrderId.text = orderModel.order_key.split("_")[2].toUpperCase()
        tvOrderDate.text = toDate(orderModel.date_created)
        if (orderModel.shipping_total == 0.0) {
            tvShippingCharge.text = getString(R.string.sh_lbl_free)
        } else {
            tvShippingCharge.text = orderModel.shipping_total.roundToInt().toString()
                .currencyFormat(orderModel.currency)
        }
        tvTotalAmount.text =
            ((orderModel.total - orderModel.discount_total) + orderModel.shipping_total).toString()
                .currencyFormat(orderModel.currency)
    }
}
