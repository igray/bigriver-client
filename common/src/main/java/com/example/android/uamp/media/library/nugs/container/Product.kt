package com.example.android.uamp.media.library.nugs.container

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Product {
    @SerializedName("productStatusType")
    @Expose
    var productStatusType: Int? = null

    @SerializedName("skuIDExt")
    @Expose
    var skuIDExt: Any? = null

    @SerializedName("formatStr")
    @Expose
    var formatStr: String? = null

    @SerializedName("skuID")
    @Expose
    var skuID: Int? = null

    @SerializedName("cost")
    @Expose
    var cost: Int? = null

    @SerializedName("costplanID")
    @Expose
    var costplanID: Int? = null

    @SerializedName("pricing")
    @Expose
    var pricing: Any? = null

    @SerializedName("bundles")
    @Expose
    var bundles: List<Any>? = null

    @SerializedName("numPublicPricePoints")
    @Expose
    var numPublicPricePoints: Int? = null

    @SerializedName("cartLink")
    @Expose
    var cartLink: String? = null

    @SerializedName("liveEventInfo")
    @Expose
    var liveEventInfo: LiveEventInfo? = null

    @SerializedName("saleWindowInfo")
    @Expose
    var saleWindowInfo: SaleWindowInfo? = null

    @SerializedName("iosCost")
    @Expose
    var iosCost: Int? = null

    @SerializedName("iosPlanName")
    @Expose
    var iosPlanName: Any? = null

    @SerializedName("googlePlanName")
    @Expose
    var googlePlanName: Any? = null

    @SerializedName("googleCost")
    @Expose
    var googleCost: Int? = null

    @SerializedName("numDiscs")
    @Expose
    var numDiscs: Int? = null

    @SerializedName("isSubStreamOnly")
    @Expose
    var isSubStreamOnly: Int? = null
}