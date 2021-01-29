package com.example.quiz.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View

import com.example.quiz.ShopHopAppBaseActivity
import com.example.quiz.R
import com.example.quiz.base.BaseRecyclerAdapter
import com.example.quiz.databinding.ShophopItemOrderBinding
import com.example.quiz.databinding.ShophopItemTrackBinding

import com.example.quiz.models.*
import com.example.quiz.utils.ShopHopConstants
import com.example.quiz.utils.extensions.*
import kotlinx.android.synthetic.main.shophop_activity_track_item.*
import kotlinx.android.synthetic.main.shophop_activity_track_item.rvOrderItems
import kotlinx.android.synthetic.main.toolbar.*
import kotlin.math.roundToInt


class ShopHopTrackItemActivity : ShopHopAppBaseActivity() {
    private lateinit var mOrderItemAdapter: BaseRecyclerAdapter<LineItem, ShophopItemOrderBinding>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shophop_activity_track_item)
        setToolbar(toolbar)
        title = getString(R.string.sh_lbl_track_item)
        disableHardwareRendering(rcvTracks)
        val orderModel = intent.getSerializableExtra(ShopHopConstants.KeyIntent.DATA) as MyOrderData
        mOrderItemAdapter = object : BaseRecyclerAdapter<LineItem, ShophopItemOrderBinding>() {
            override val layoutResId: Int get() = R.layout.shophop_item_order

            @SuppressLint("SetTextI18n")
            override fun onBindData(model: LineItem, position: Int, dataBinding: ShophopItemOrderBinding) {
                dataBinding.tvPrice.text = model.total.roundToInt().toString().currencyFormat(orderModel.currency)
                dataBinding.tvOriginalPrice.text = model.price.toString().currencyFormat(orderModel.currency)
                dataBinding.tvTotalItem.text = getString(R.string.sh_text_total_item_1) + model.quantity
                dataBinding.tvOriginalPrice.applyStrike()
                if (model.color.isNotEmpty()) {
                    dataBinding.ivChecked.changeBackgroundTint(android.graphics.Color.parseColor(model.color))
                }
                if (model.image.isNotEmpty()) {
                    dataBinding.ivProduct.loadImageFromUrl(model.image)
                }
            }

            override fun onItemClick(view: View, model: LineItem, position: Int, dataBinding: ShophopItemOrderBinding) {}
            override fun onItemLongClick(view: View, model: LineItem, position: Int) {}

        }

        rvOrderItems.setVerticalLayout()
        rvOrderItems.adapter = mOrderItemAdapter
        rcvTracks.setVerticalLayout()
        rcvTracks.adapter = mTracksAdapter
        mOrderItemAdapter.addItems(orderModel.line_items)
        getOrderTrackings()
        btnCancelOrder.hide()
//        loadBannerAd(R.id.adView)

    }

    private fun getOrderTrackings() {
        addData()
    }

    var mTracksAdapter = object : BaseRecyclerAdapter<Track, ShophopItemTrackBinding>() {
        override val layoutResId: Int = R.layout.shophop_item_track

        override fun onBindData(model: Track, position: Int, dataBinding: ShophopItemTrackBinding) {
            if (model.status == ShopHopTrackStatus.NOTDONE) {
                dataBinding.tvText1.text = ""
                dataBinding.tvDate.text = ""
            } else {
                if (model.status == ShopHopTrackStatus.OUTFORDELIVERY && !(model.isDone!!)) {
                    dataBinding.tvText1.text = ""
                    dataBinding.tvDate.text = ""
                } else {
                    dataBinding.tvText1.text = model.trackStatus!!.getHtmlString()
                    dataBinding.tvDate.text = model.date
                }
            }

            val color = color(getTrackColor(model.status))
            dataBinding.ivCircle.setCircleColor(color)

            if (model.isActive!!) {
                dataBinding.ivCircle.setRadius(19f)
            } else {
                dataBinding.ivCircle.setRadius(14f)
            }
            if (model.isDone!!) {
                dataBinding.ivLine.setLineColor(color)
            } else {
                dataBinding.ivLine.setLineColor(color(R.color.ShopHop_track_grey))
            }
            if (model.status == ShopHopTrackStatus.OUTFORDELIVERY) {
                dataBinding.ivCircle.hide()
                dataBinding.tvDate.hide()
                dataBinding.tvText1.setTextColor(color(R.color.ShopHop_textColorSecondary))
            } else {
                dataBinding.ivCircle.show()
                dataBinding.tvDate.show()
                dataBinding.tvText1.setTextColor(color(R.color.ShopHop_textColorPrimary))


            }
            if (model.status == ShopHopTrackStatus.DELIVERED || model.status == ShopHopTrackStatus.DELIVERING) {
                dataBinding.tvText1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.shophop_ic_keyboard_arrow_right_black, 0)
                dataBinding.ivLine.hide()
            } else {
                dataBinding.tvText1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                dataBinding.ivLine.show()
            }

        }

        override fun onItemClick(view: View, model: Track, position: Int, dataBinding: ShophopItemTrackBinding) {
        }

        override fun onItemLongClick(view: View, model: Track, position: Int) {

        }
    }

    private fun getTrackColor(status: ShopHopTrackStatus?): Int {
        when (status) {
            ShopHopTrackStatus.NOTDONE -> {
                return R.color.ShopHop_track_grey
            }
            ShopHopTrackStatus.SHIPPED -> {
                return R.color.ShopHop_track_green
            }
            ShopHopTrackStatus.ARRIVED -> {
                return R.color.ShopHop_track_green
            }
            ShopHopTrackStatus.OUTFORDELIVERY -> {
                return R.color.ShopHop_track_green
            }
            ShopHopTrackStatus.DELIVERED -> {
                return R.color.ShopHop_track_green
            }
            ShopHopTrackStatus.PENDING -> {
                return R.color.ShopHop_track_yellow
            }
            else -> return R.color.ShopHop_track_grey
        }
    }

    private fun addData() {
        val tracks = ArrayList<Track>()
        val track = Track()
        track.status = ShopHopTrackStatus.SHIPPED
        track.trackStatus = "Order <font color=#64B931>Shipped</font>"
        track.date = "Mon,17 July 2019"

        val track2 = Track()
        track2.status = ShopHopTrackStatus.ARRIVED
        track2.trackStatus = "Item <font color=#64B931>Arrived</font> at Mumbai"
        track2.date = "Mon,17 July 2019"

        val track3 = Track()
        track3.status = ShopHopTrackStatus.ARRIVED
        track3.trackStatus = "Item <font color=#64B931>Arrived</font> at Ahmedabad"
        track3.date = "Mon,17 July 2019"

        val track4 = Track()
        track4.status = ShopHopTrackStatus.OUTFORDELIVERY
        track4.trackStatus = "Your order is out for delivery"
        track4.date = "Mon,17 July 2019"

        val track5 = Track()
        track5.status = ShopHopTrackStatus.ARRIVED
        track5.trackStatus = "Item <font color=#64B931>Arrived</font> at Anand"
        track5.date = "Mon,17 July 2019"

        val track6 = Track()
        track6.status = ShopHopTrackStatus.DELIVERED
        // track6.trackStatus = "Item Delivering"
        track6.trackStatus = "Item <font color=#64B931>Delivered</font>"
        track6.date = "Mon,17 July 2019"

        tracks.add(track)
        tracks.add(track2)
        tracks.add(track3)
        tracks.add(track4)
        tracks.add(track5)
        tracks.add(track6)
        mTracksAdapter.addItems(tracks)
    }
}
