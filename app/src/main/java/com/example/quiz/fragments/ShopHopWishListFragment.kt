package com.example.quiz.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.quiz.ShopHopAppBaseActivity
import com.example.quiz.R
import com.example.quiz.base.BaseRecyclerAdapter
import com.example.quiz.databinding.ShophopItemWishlistBinding
import com.example.quiz.models.Key
import com.example.quiz.models.WishList
import com.example.quiz.utils.extensions.*
import kotlinx.android.synthetic.main.shophop_fragment_wishlist.*
import kotlinx.android.synthetic.main.shophop_layout_nodata.*


class ShopHopWishListFragment : ShopHopBaseFragment() {
    private val mListAdapter = getAdapter()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.shophop_fragment_wishlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvWishList.adapter = mListAdapter
        rvWishList.rvItemAnimation()
    }

    private fun getAdapter(): BaseRecyclerAdapter<WishList, ShophopItemWishlistBinding> {

        return object : BaseRecyclerAdapter<WishList, ShophopItemWishlistBinding>() {

            override fun onItemClick(view: View, model: WishList, position: Int, dataBinding: ShophopItemWishlistBinding) {
                when (view.id) {
                    R.id.btnRemove -> {
                        (activity as ShopHopAppBaseActivity).removeWishList(model.item_id) {
                            snackBar("Successfully remove")
                        }
                    }
                    R.id.llMoveToCart -> {
                        val cartData = Key()
                        cartData.product_id = model.product_id
                        cartData.product_name = model.name!!
                        cartData.sale_price = model.sale_price!!
                        cartData.product_price = model.regular_price!!
                        cartData.quantity = 1
                        cartData.product_image = model.image!!
                        //cartData.product_color = color
                        // cartData.product_size = size
                        (activity as ShopHopAppBaseActivity).addCart(cartData)
                        (activity as ShopHopAppBaseActivity).removeWishList(model.item_id) {
                            snackBar("Successfully remove")
                        }
                    }
                }
            }

            override val layoutResId: Int = R.layout.shophop_item_wishlist

            override fun onBindData(model: WishList, position: Int, dataBinding: ShophopItemWishlistBinding) {
                if (activity !== null) {
                    dataBinding.tvProductPrice.text = model.sale_price?.currencyFormat()
                    dataBinding.tvProductActualPrice.text = model.regular_price?.currencyFormat()
                    dataBinding.tvProductActualPrice.applyStrike()
                    if (model.image != null) {
                        if (model.image?.isNotEmpty()!!) {
                            dataBinding.ivProduct.loadImageFromUrl(model.image!!)
                        }
                    }

                }


            }

            override fun onItemLongClick(view: View, model: WishList, position: Int) {}
        }
    }

    override fun onResume() {
        super.onResume()
        wishListItemChange()
    }

    fun wishListItemChange() {
        if (rvWishList != null) {
            val mWishList = getWishlist()
            mListAdapter.addItems(mWishList)
            if (mWishList.size == 0) rlNoData.show()
            else rlNoData.hide()
        }
    }
}
