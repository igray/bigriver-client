package org.igster.bigriver.client.media.library.nugs.containers

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ProductFormat {
    @SerializedName("pfType")
    @Expose
    var pfType: Int? = null

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

    @SerializedName("pfTypeStr")
    @Expose
    var pfTypeStr: String? = null

    @SerializedName("liveEvent")
    @Expose
    var liveEvent: LiveEvent? = null

    @SerializedName("salewindow")
    @Expose
    var salewindow: Salewindow? = null

    @SerializedName("skuCode")
    @Expose
    var skuCode: String? = null

    @SerializedName("isSubStreamOnly")
    @Expose
    var isSubStreamOnly: Int? = null
}