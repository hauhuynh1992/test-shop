package com.example.quiz.fragments

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import android.widget.RelativeLayout
import androidx.databinding.DataBindingUtil

import com.example.quiz.R
import com.example.quiz.activity.ShopHopProductDetailActivity
import com.example.quiz.base.BaseRecyclerAdapter
import com.example.quiz.databinding.ShophopFragmentItemreviewBinding
import com.example.quiz.databinding.ShophopItemReviewBinding

import com.example.quiz.models.ProductModel
import com.example.quiz.models.ProductReviewData
import com.example.quiz.models.ShopHopRequestModel
import com.example.quiz.utils.ShopHopConstants
import com.example.quiz.utils.ShopHopConstants.SharedPref.USER_EMAIL
import com.example.quiz.utils.ShopHopConstants.SharedPref.USER_FIRST_NAME
import com.example.quiz.utils.extensions.*
import kotlinx.android.synthetic.main.shophop_dialog_rate.*
import kotlinx.android.synthetic.main.shophop_fragment_itemreview.*
import kotlinx.android.synthetic.main.shophop_layout_nodata.*


class ShopHopReviewFragment : ShopHopBaseFragment() {
    companion object {
        fun getNewInstance(model: ProductModel): ShopHopReviewFragment {
            val fragment = ShopHopReviewFragment()
            val bundle = Bundle()
            bundle.putSerializable(ShopHopConstants.KeyIntent.DATA, model)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var mainBinding: ShophopFragmentItemreviewBinding
    private lateinit var mProductModels: ProductModel
    private lateinit var mProductReviewData: ProductReviewData
    private val mReviewAdapter =
            object : BaseRecyclerAdapter<ProductReviewData, ShophopItemReviewBinding>() {
                override fun onItemLongClick(view: View, model: ProductReviewData, position: Int) {}

                override fun onItemClick(
                        view: View,
                        model: ProductReviewData,
                        position: Int,
                        dataBinding: ShophopItemReviewBinding
                ) {
                    mProductReviewData = model
                    if (view.id == R.id.ivDelete) {
                        val popup = PopupMenu(context, view)
                        popup.menuInflater.inflate(R.menu.shophop_menu_review, popup.menu)
                        popup.setOnMenuItemClickListener { item ->
                            when (item!!.itemId) {
                                R.id.nav_delete -> confirmDialog()

                                R.id.nav_update -> updateReview()
                            }
                            true
                        }
                        popup.show()
                    }
                }

                override val layoutResId: Int = R.layout.shophop_item_review

                override fun onBindData(
                        model: ProductReviewData,
                        position: Int,
                        dataBinding: ShophopItemReviewBinding
                ) {
                    mProductReviewData = model
                    dataBinding.tvProductReviewRating.text = model.rating.toString()
                    dataBinding.tvProductReviewSubHeading.text = model.review.getHtmlString()
                    dataBinding.tvProductReviewCmt.text = model.name
                    dataBinding.tvProductReviewImg.loadImageFromUrl(mProductModels.images[0].src)
                    dataBinding.tvProductReviewDuration.text = toDate(model.date_created)
                    if (!model.verified) {
                        dataBinding.tvProductReviewVerified.setCompoundDrawablesWithIntrinsicBounds(R.drawable.shophop_ic_round_cancel_24px, 0, 0, 0)
                        dataBinding.tvProductReviewVerified.text = getString(R.string.sh_lbl_not_verified)
                    } else {
                        dataBinding.tvProductReviewVerified.setCompoundDrawablesWithIntrinsicBounds(R.drawable.shophop_ic_checkbox_circle_fill, 0, 0, 0)
                        dataBinding.tvProductReviewVerified.text = getString(R.string.sh_lbl_verified)
                    }
                    if (model.rating == 1) {
                        dataBinding.tvProductReviewRating.setStrokedBackground(activity!!.color(R.color.ShopHop_red))
                    } else if (model.rating == 2 || model.rating == 3) {
                        dataBinding.tvProductReviewRating.setStrokedBackground(activity!!.color(R.color.ShopHop_yellow))
                    } else if (model.rating == 5 || model.rating == 4) {
                        dataBinding.tvProductReviewRating.setStrokedBackground(activity!!.color(R.color.ShopHop_bg_4star))
                    }
                    if (isLoggedIn() && model.email == getSharedPrefInstance().getStringValue(USER_EMAIL, "")
                    ) {
                        dataBinding.ivDelete.show()
                    }

                }
            }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        mainBinding =
                DataBindingUtil.inflate(inflater, R.layout.shophop_fragment_itemreview, container, false)
        return mainBinding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.shophop_menu_review, menu)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mProductModels = arguments?.getSerializable(ShopHopConstants.KeyIntent.DATA) as ProductModel

        mainBinding.model = mProductModels
        if (isLoggedIn()) {
            btnRateNow.show()
        }
        ivBackground.setStrokedBackground(
                activity!!.color(R.color.ShopHop_favourite_unselected_background),
                activity!!.color(R.color.ShopHop_dots_color)
        )
        rvReview.adapter = mReviewAdapter
        listProductReviews()

        btnRateNow.onClick {
            createProductReview()
        }
        sb1Star.setOnTouchListener { _, _ -> true }
        sb2Star.setOnTouchListener { _, _ -> true }
        sb3Star.setOnTouchListener { _, _ -> true }
        sb4Star.setOnTouchListener { _, _ -> true }
        sb5Star.setOnTouchListener { _, _ -> true }

    }

    private fun setRating(data: List<ProductReviewData>) {

        var fiveStar = 0
        var fourStar = 0
        var threeStar = 0
        var twoStar = 0
        var oneStar = 0
        for (reviewModel in data) {
            when (reviewModel.rating) {
                5 -> fiveStar++
                4 -> fourStar++
                3 -> threeStar++
                2 -> twoStar++
                1 -> oneStar++
            }
        }

        if (activity != null) {
            sb1Star.max = data.size
            sb2Star.max = data.size
            sb3Star.max = data.size
            sb4Star.max = data.size
            sb5Star.max = data.size

            sb1Star.progress = oneStar
            sb2Star.progress = twoStar
            sb3Star.progress = threeStar
            sb4Star.progress = fourStar
            sb5Star.progress = fiveStar


            tvTotalReview.text = String.format("%d Reviews", data.size)
            tv5Count.text = fiveStar.toString()
            tv4Count.text = fourStar.toString()
            tv3Count.text = threeStar.toString()
            tv2Count.text = twoStar.toString()
            tv1Count.text = oneStar.toString()
            val mAvgRating =
                    (5 * fiveStar + 4 * fourStar + 3 * threeStar + 2 * twoStar + 1 * oneStar) / (fiveStar + fourStar + threeStar + twoStar + oneStar)
            tvReviewRate.text = mAvgRating.toString()
        }
    }

    private fun createProductReview() {
        if (activity != null) {
            val dialog = Dialog(activity!!)
            dialog.window?.setBackgroundDrawable(ColorDrawable(0))
            dialog.setContentView(R.layout.shophop_dialog_rate)
            dialog.setCanceledOnTouchOutside(false)

            dialog.window?.setLayout(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            dialog.btnSubmit.onClick {
                if (isNetworkAvailable()) {
                    val requestModel = ShopHopRequestModel()

                    requestModel.product_id = mProductModels.id.toString()
                    requestModel.reviewer =
                            getSharedPrefInstance().getStringValue(USER_FIRST_NAME, "")
                    requestModel.reviewer_email =
                            getSharedPrefInstance().getStringValue(USER_EMAIL, "")
                    requestModel.review = dialog.edtReview.textToString()
                    requestModel.rating = dialog.ratingBar.rating.toInt().toString()

                    addUserReview(requestModel, onApiSuccess = {
                        dialog.dismiss()
                        listProductReviews()
                        btnRateNow.hide()

                    })
                }
            }
            dialog.viewCloseDialog.onClick {
                dialog.dismiss()
            }
            dialog.show()
        }
    }

    fun confirmDialog() {
        if (activity != null) {
            activity!!.getAlertDialog(
                    getString(R.string.sh_msg_confirmation),
                    onPositiveClick = { _, _ ->
                        deleteUserReview(mProductReviewData, onApiSuccess = {
                            snackBar(activity!!.getString(R.string.sh_success))
                            listProductReviews()
                            btnRateNow.show()
                        })
                    },
                    onNegativeClick = { dialog, _ ->
                        dialog.dismiss()
                    }).show()
        }
    }

    fun updateReview() {
        if (activity != null) {
            val dialog = Dialog(activity!!)
            dialog.window?.setBackgroundDrawable(ColorDrawable(0))
            dialog.setContentView(R.layout.shophop_dialog_rate)

            dialog.window?.setLayout(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            dialog.edtReview.setText(mProductReviewData.review.getHtmlString())
            dialog.ratingBar.rating = mProductReviewData.rating.toFloat()
            dialog.btnSubmit.onClick {
                if (isNetworkAvailable()) {
                    val requestModel = ShopHopRequestModel()

                    requestModel.product_id = mProductModels.id.toString()
                    requestModel.reviewer = getFirstName()
                    requestModel.reviewer_email = getEmail()
                    requestModel.review = dialog.edtReview.textToString()
                    requestModel.rating = dialog.ratingBar.rating.toInt().toString()
                    addUserReview(requestModel, onApiSuccess = {
                        //toast("Successfully")
                        dialog.dismiss()
                        listProductReviews()
                    })
                }
            }
            dialog.show()
        }
    }

    private fun listProductReviews() {
        getProductsReviews(mProductModels.id, onApiSuccess = {
            (activity as ShopHopProductDetailActivity).setReviews(it)
            if (it.isEmpty()) {
                if (activity != null) {
                    rlNoData.show()
                    rvReview.hide()
                }

            } else {
                mReviewAdapter.addItems(it)
                setRating(it)
                rvReview.show()
            }
        }, userReviewed = {
            if (it) {
                btnRateNow.hide()
            } else if (isLoggedIn()) {
                btnRateNow.show()
            }
        })
    }

}