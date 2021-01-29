package com.example.quiz.fragments


import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.quiz.ShopHopAppBaseActivity
import com.example.quiz.R
import com.example.quiz.activity.ShopHopDashBoardActivity
import com.example.quiz.activity.ShopHopOrderSummaryActivity
import com.example.quiz.base.BaseRecyclerAdapter
import com.example.quiz.databinding.ShophopItemCartBinding
import com.example.quiz.models.Key
import com.example.quiz.utils.extensions.*
import kotlinx.android.synthetic.main.shophop_fragment_cart.*


class ShopHopMyCartFragment : ShopHopBaseFragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.shophop_fragment_cart, container, false)
    }

    private var mCartAdapter: BaseRecyclerAdapter<Key, ShophopItemCartBinding> =
            object : BaseRecyclerAdapter<Key, ShophopItemCartBinding>() {
                override val layoutResId: Int = R.layout.shophop_item_cart

                override fun onBindData(model: Key, position: Int, dataBinding: ShophopItemCartBinding) {
                    dataBinding.llButton.show()
                    dataBinding.llMoveTocart.hide()
                    dataBinding.llNextTimeBuy.show()
                    dataBinding.tvOriginalPrice.applyStrike()
                    if (model.sale_price.isNotEmpty()) {
                        dataBinding.tvPrice.text =
                                (model.sale_price.toInt() * model.quantity).toString().currencyFormat()
                    }
                    if (model.product_price.isNotEmpty()) {
                        dataBinding.tvOriginalPrice.text =
                                (model.product_price.toInt() * model.quantity).toString().currencyFormat()
                    }
                    if (model.product_color.isNotEmpty()) {
                        dataBinding.ivChecked.changeBackgroundTint(
                                android.graphics.Color.parseColor(
                                        model.product_color
                                )
                        )
                    }
                    dataBinding.ivProduct.loadImageFromUrl(model.product_image)
                    dataBinding.qtySpinner.text = model.quantity.toString()
                }

                override fun onItemClick(view: View, model: Key, position: Int, dataBinding: ShophopItemCartBinding) {
                    when (view.id) {
                        R.id.llRemove -> {
                            removeCartItem(model, false)
                        }
                        R.id.ll_qty -> activity?.showQtyChangeDialog {
                            model.quantity = it.toInt()
                            notifyItemChanged(position)
                            updateCartItem(model)
                        }
                        R.id.llNextTimeBuy -> {
                            removeCartItem(model, true)
                        }
                    }
                }

                override fun onItemLongClick(view: View, model: Key, position: Int) {}
            }

    private var mNextTimeBuyAdapter = object : BaseRecyclerAdapter<Key, ShophopItemCartBinding>() {
        override val layoutResId: Int = R.layout.shophop_item_cart

        override fun onBindData(model: Key, position: Int, dataBinding: ShophopItemCartBinding) {
            dataBinding.llButton.show()
            dataBinding.llMoveTocart.show()
            dataBinding.llNextTimeBuy.hide()
            dataBinding.ivProduct.loadImageFromUrl(model.product_image)
            dataBinding.tvOriginalPrice.applyStrike()
            dataBinding.tvPrice.text = model.sale_price.currencyFormat()
            dataBinding.tvOriginalPrice.text = model.product_price.currencyFormat()
            if (model.product_color.isNotEmpty()){
                dataBinding.ivChecked.changeBackgroundTint(android.graphics.Color.parseColor(model.product_color))
            }
            dataBinding.qtySpinner.text = model.quantity.toString()
        }

        override fun onItemClick(view: View, model: Key, position: Int, dataBinding: ShophopItemCartBinding) {
            when (view.id) {
                R.id.ll_qty -> activity?.showQtyChangeDialog {
                    mModelList[position].quantity = it.toInt()
                    notifyItemChanged(position)
                }
                R.id.llMoveTocart -> {
                    removeItem(position)
                    moveItemToCart(model)
                }
                R.id.llRemove -> {
                    removeItem(position)
                    setNextTimeBuyProducts(mModelList)
                    if (itemCount == 0) {
                        tvNextTimeBuy.hide()
                    } else {
                        tvNextTimeBuy.show()
                    }

                }
            }
        }

        override fun onItemLongClick(view: View, model: Key, position: Int) {

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        rvCart.setVerticalLayout()
        rvNextTimeBuy.setVerticalLayout()
        rvCart.adapter = mCartAdapter
        rvNextTimeBuy.adapter = mNextTimeBuyAdapter
        btnShopNow.onClick {
            if (activity!! is ShopHopDashBoardActivity) {
                (activity!! as ShopHopDashBoardActivity).loadHomeFragment()
            }

        }
        tvContinue.onClick {
            activity?.launchActivity<ShopHopOrderSummaryActivity> { }
        }
        llSeePriceDetail.onClick {
            scrollToPriceDetail()
        }
    }

    private fun setItems() {
        mNextTimeBuyAdapter.addItems(nextTimeBuyProducts())
        setCart()

    }

    private fun scrollToPriceDetail() {
        Handler().post {
            nsvCart.scrollTo(0, llPayment.top)
        }
    }


    private fun invalidateCartLayout(it: ArrayList<Key>) {
        if (activity != null) {
            if (it.size == 0) {
                invalidatePaymentLayout(false)
                if (mNextTimeBuyAdapter.itemCount == 0) {
                    llNoItems.show()
                    tvNextTimeBuy.hide()

                } else {
                    llNoItems.hide()
                    tvNextTimeBuy.show()
                }
            } else {
                llNoItems.hide()
                if (mNextTimeBuyAdapter.itemCount == 0) {
                    tvNextTimeBuy.hide()
                } else {
                    tvNextTimeBuy.show()
                }
                tvTotalCartAmount.text = (activity as ShopHopAppBaseActivity).getCartTotal().toString().currencyFormat()
                invalidatePaymentLayout(true)
                mCartAdapter.addItems(it)
            }

        }
    }

    private fun invalidatePaymentLayout(b: Boolean) {
        if (activity != null) {
            if (!b) {
                llPayment.hide()
                lay_button.hide()
                rvCart.hide()
            } else {
                llPayment.show()
                lay_button.show()
                rvCart.show()
            }
        }

    }

    private fun removeCartItem(model: Key, shouldNextTimeBuy: Boolean) {
        activity!!.getAlertDialog(
                getString(R.string.sh_msg_confirmation),
                onPositiveClick = { _, _ ->
                    val key = Key()
                    key.product_id = model.product_id
                    (activity as ShopHopAppBaseActivity).removeItem(key)
                    snackBar(activity!!.getString(R.string.sh_success))
                    if (shouldNextTimeBuy) {
                        tvNextTimeBuy.show()
                        mNextTimeBuyAdapter.addNewItem(model)
                        addNextTimeBuyProduct(model)
                    }
                },
                onNegativeClick = { dialog, _ ->
                    dialog.dismiss()
                }).show()
    }

    private fun moveItemToCart(model: Key) {
        (activity as ShopHopAppBaseActivity).addCart(model)
        setNextTimeBuyProducts(mNextTimeBuyAdapter.mModelList)
    }

    private fun updateCartItem(model: Key) {
        updateItem(model)
        tvTotalCartAmount.text = (activity as ShopHopAppBaseActivity).getCartTotal().toString().currencyFormat()
    }

    override fun onResume() {
        super.onResume()
        setItems()
    }
    fun setCart() {
        if (activity!=null){
            invalidateCartLayout(getCartList())
        }
    }
}
