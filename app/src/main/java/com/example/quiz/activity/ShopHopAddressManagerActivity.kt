package com.example.quiz.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.quiz.ShopHopAppBaseActivity
import com.example.quiz.R
import com.example.quiz.base.BaseRecyclerAdapter
import com.example.quiz.databinding.ShophopItemAddressNewBinding
import com.example.quiz.models.Address
import com.example.quiz.utils.ShopHopConstants
import com.example.quiz.utils.extensions.*
import kotlinx.android.synthetic.main.shophop_activity_address_manager.*
import kotlinx.android.synthetic.main.toolbar.*

class ShopHopAddressManagerActivity : ShopHopAppBaseActivity() {

    private var addressAdapter = object : BaseRecyclerAdapter<Address, ShophopItemAddressNewBinding>() {
        override val layoutResId: Int = R.layout.shophop_item_address_new

        override fun onBindData(model: Address, position: Int, dataBinding: ShophopItemAddressNewBinding) {
            dataBinding.rbDefaultAddress.isChecked = model.isDefault!!
            dataBinding.included.tvUserName.text = model.fullName
            dataBinding.included.tvTypeAddress.text = model.addressType
            dataBinding.included.tvAddress.text = model.address
            dataBinding.included.tvMobileNo.text = model.mobileNo
        }

        override fun onItemClick(view: View, model: Address, position: Int, dataBinding: ShophopItemAddressNewBinding) {
            when (view.id) {
                R.id.addressLayout -> {
                    setDefaultAddress(position)
                }
                R.id.fabEdit -> {
                    dataBinding.swipeLayout.close(true)
                    runDelayed(200) {
                        launchActivity<ShopHopAddAddressActivity>(ShopHopConstants.RequestCode.ADD_ADDRESS) {
                            putExtra(ShopHopConstants.KeyIntent.DATA, model)
                            putExtra(ShopHopConstants.KeyIntent.ADDRESS_ID, position)
                        }
                    }
                }
                R.id.fabDelete -> {
                    getAlertDialog(getString(R.string.sh_msg_confirmation), onPositiveClick = { _, _ ->
                        mModelList.removeAt(position)
                        notifyItemRemoved(position)
                        setAddressList(mModelList)
                    }, onNegativeClick = { dialog, _ ->
                        dialog.dismiss()
                    }).show()
                }
            }
        }

        override fun onItemLongClick(view: View, model: Address, position: Int) {

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shophop_activity_address_manager)
        setToolbar(toolbar)
        title = getString(R.string.sh_lbl_address_manager)
        rvAddress.setVerticalLayout()
        rvAddress.adapter = addressAdapter
        loadAddressList()
        btnAddNew.onClick {
            launchActivity<ShopHopAddAddressActivity>(ShopHopConstants.RequestCode.ADD_ADDRESS)
        }
//        loadBannerAd(R.id.adView)

    }

    private fun loadAddressList() {
        addressAdapter.addItems(getAddressList())
    }

    private fun setDefaultAddress(position: Int) {
        addressAdapter.mModelList.forEachIndexed { i: Int, address: Address ->
            address.isDefault = i == position
        }
        addressAdapter.notifyDataSetChanged()
        setAddressList(addressAdapter.mModelList)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ShopHopConstants.RequestCode.ADD_ADDRESS && resultCode == Activity.RESULT_OK) {
            loadAddressList()
        }
    }
}