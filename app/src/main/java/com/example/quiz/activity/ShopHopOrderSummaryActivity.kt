package com.example.quiz.activity

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.RelativeLayout
import com.example.quiz.ShopHopAppBaseActivity
import com.example.quiz.R
import com.example.quiz.base.BaseRecyclerAdapter
import com.example.quiz.databinding.ShophopItemCartBinding
import com.example.quiz.databinding.ShophopItemUserAddressBinding
import com.example.quiz.models.*
import com.example.quiz.utils.ShopHopConstants
import com.example.quiz.utils.extensions.*
import kotlinx.android.synthetic.main.shophop_activity_order_summary.*
import kotlinx.android.synthetic.main.shophop_dialog_change_address.*
import kotlinx.android.synthetic.main.shophop_item_address.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*

class ShopHopOrderSummaryActivity : ShopHopAppBaseActivity() {

    private lateinit var dialog: Dialog
    private var mAddressAdapter = object : BaseRecyclerAdapter<Address, ShophopItemUserAddressBinding>() {
        override val layoutResId: Int
            get() = R.layout.shophop_item_user_address

        override fun onBindData(model: Address, position: Int, dataBinding: ShophopItemUserAddressBinding) {

            dataBinding.rbDefaultAddress.isChecked = model.isDefault!!
            if (model.isDefault!!) {
                dataBinding.included.tvEdit.show()
            } else {
                dataBinding.included.tvEdit.hide()
            }
            dataBinding.included.tvUserName.text = model.fullName
            dataBinding.included.tvTypeAddress.text = model.addressType
            dataBinding.included.tvAddress.text = model.address
            dataBinding.included.tvMobileNo.text = model.mobileNo
            dataBinding.included.tvEdit.onClick {
                launchActivity<ShopHopAddAddressActivity>(ShopHopConstants.RequestCode.ADD_ADDRESS) {
                    putExtra(ShopHopConstants.KeyIntent.DATA, model)
                    putExtra(ShopHopConstants.KeyIntent.ADDRESS_ID, position)
                }
            }
        }

        override fun onItemClick(view: View, model: Address, position: Int, dataBinding: ShophopItemUserAddressBinding) {
            setDefaultAddress(position)
        }

        override fun onItemLongClick(view: View, model: Address, position: Int) {

        }

    }

    private var cartAdapter: BaseRecyclerAdapter<Key, ShophopItemCartBinding> =
        object : BaseRecyclerAdapter<Key, ShophopItemCartBinding>() {
            override val layoutResId: Int = R.layout.shophop_item_cart

            override fun onBindData(model: Key, position: Int, dataBinding: ShophopItemCartBinding) {
                dataBinding.tvOriginalPrice.applyStrike()
                dataBinding.ivDropDown.visibility=View.INVISIBLE
                dataBinding.qtySpinner.text = model.quantity.toString()
                dataBinding.tvOriginalPrice.applyStrike()
                if (model.sale_price.isNotEmpty()) {
                    dataBinding.tvPrice.text =
                        (model.sale_price.toInt() * model.quantity).toString().currencyFormat()
                }
                if (model.product_price.isNotEmpty()) {
                    dataBinding.tvOriginalPrice.text =
                        (model.product_price.toInt() * model.quantity).toString().currencyFormat()
                }
                dataBinding.ivProduct.loadImageFromUrl(model.product_image)
            }

            override fun onItemClick(
                view: View,
                model: Key,
                position: Int,
                dataBinding: ShophopItemCartBinding
            ) {
            }

            override fun onItemLongClick(view: View, model: Key, position: Int) {

            }
        }
    private val mImg = ArrayList<String>()
    private var total = 0

    private fun setDefaultAddress(position: Int) {
        mAddressAdapter.mModelList.forEachIndexed { i: Int, address: Address ->
            address.isDefault = i == position
        }
        mAddressAdapter.notifyDataSetChanged()
        setAddressList(mAddressAdapter.mModelList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shophop_activity_order_summary)
        setToolbar(toolbar)
        title = getString(R.string.sh_order_summary)
        getOffers()
        rvItems.setVerticalLayout()
        rvItems.adapter = cartAdapter
        cartAdapter.addItems(getCartList())
        total = getCartTotal()

        initChangeAddressDialog()
        btnChangeAddress.onClick {
            if (mAddressAdapter.size == 0) {
                launchActivity<ShopHopAddAddressActivity>(ShopHopConstants.RequestCode.ADD_ADDRESS)
            } else {
                dialog.show()
            }
        }
        val mPaymentDetail = getCartTotal()
        tvReset.text = mPaymentDetail.toString().currencyFormat()
        tvApply.onClick {
            createOrder()
        }
        if (getAddressList().size == 0) {
            launchActivity<ShopHopAddAddressActivity>(ShopHopConstants.RequestCode.ADD_ADDRESS)
            llAddress.hide()
        } else {
            llAddress.show()
        }
//        loadBannerAd(R.id.adView)

    }

    private fun getOffers() {
        listAllProducts {
            it.forEach {
                if (it.images.isNotEmpty()) {
                    mImg.add(it.images[0].src)
                }
            }
            val handler = Handler()
            val runnable = object : Runnable {
                var i = 0
                override fun run() {
                    ivOffer.loadImageFromUrl(mImg[i])
                    i++
                    if (i > mImg.size - 1) {
                        i = 0
                    }
                    handler.postDelayed(this, 3000)
                }
            }
            handler.postDelayed(runnable, 3000)

        }
    }

    private fun initChangeAddressDialog() {
        dialog = Dialog(this)
        dialog.window?.setBackgroundDrawable(ColorDrawable(0))
        dialog.setContentView(R.layout.shophop_dialog_change_address)
        dialog.window?.setLayout(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        dialog.tvAddNewAddress.onClick {
            launchActivity<ShopHopAddAddressActivity>(ShopHopConstants.RequestCode.ADD_ADDRESS)
        }
        dialog.tvItemDeliverHere.onClick {
            dialog.dismiss()
            llAddress.show()
            updateAddress()
        }
        dialog.rvAddress.setVerticalLayout()
        dialog.rvAddress.adapter = mAddressAdapter
        loadAddressList()
        updateAddress()
    }

    private fun updateAddress() {
        mAddressAdapter.mModelList.forEach {
            if (it.isDefault!!) {
                tvUserName.text = it.fullName
                tvTypeAddress.text = it.addressType
                tvAddress.text = it.address
                tvMobileNo.text = it.mobileNo
            }
        }
    }

    private fun loadAddressList() {
        mAddressAdapter.addItems(getAddressList())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ShopHopConstants.RequestCode.ADD_ADDRESS) {
            loadAddressList()
            if (mAddressAdapter.itemCount == 0) {
                finish()
            } else {
                dialog.show()
            }
        } else if (requestCode == ShopHopConstants.RequestCode.PAYMENT && resultCode == Activity.RESULT_OK) {
            getSharedPrefInstance().removeKey(ShopHopConstants.SharedPref.KEY_USER_CART)
            getSharedPrefInstance().removeKey(ShopHopConstants.SharedPref.KEY_CART_COUNT)
            setResult(Activity.RESULT_OK)
            finish()
        }

    }

    private fun createOrder() {
        val requestModel = MyOrderData()
        val mData = ArrayList<LineItem>()

        getCartList().forEach {
            val mlineitem = LineItem()
            mlineitem.product_id = it.product_id
            mlineitem.quantity = it.quantity
            mlineitem.variation_id = it.variation_id
            mlineitem.image = it.product_image
            mlineitem.name = it.product_name
            mlineitem.total = it.product_price.toDouble()
            mlineitem.quantity = it.quantity
            mlineitem.size = it.product_size
            mlineitem.color = it.product_color
            mData.add(mlineitem)
        }
        requestModel.line_items = mData
        mAddressAdapter.mModelList.forEach {
            if (it.isDefault!!) {
                val mShippingRequest = ShopHopShipping()
                mShippingRequest.first_name = it.fullName!!
                mShippingRequest.last_name = it.fullName!!
                mShippingRequest.address_1 = it.address!!
                mShippingRequest.address_2 = it.address!!
                mShippingRequest.city = it.city!!
                mShippingRequest.state = it.state!!
                mShippingRequest.postcode = it.pincode!!
                mShippingRequest.country = it.state!!
                requestModel.shipping = mShippingRequest
            }

        }
        requestModel.customer_id = getUserId().toInt()
        requestModel.status = ShopHopConstants.OrderStatus.PENDING
        requestModel.total = total.toDouble()
        requestModel.date_created = ShopHopConstants.FULL_DATE_FORMATTER.format(Date())
        dialog.dismiss()
        launchActivity<ShopHopPaymentActivity>(ShopHopConstants.RequestCode.PAYMENT) {
            putExtra(ShopHopConstants.KeyIntent.DATA, requestModel)
        }
    }


}