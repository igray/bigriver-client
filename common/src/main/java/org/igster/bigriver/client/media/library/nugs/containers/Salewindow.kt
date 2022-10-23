package org.igster.bigriver.client.media.library.nugs.containers

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Salewindow {
    @SerializedName("sswID")
    @Expose
    var sswID: Int? = null

    @SerializedName("timeZoneToDisplay")
    @Expose
    var timeZoneToDisplay: String? = null

    @SerializedName("offsetFromLocalTimeToDisplay")
    @Expose
    var offsetFromLocalTimeToDisplay: Int? = null

    @SerializedName("saleStartDateStr")
    @Expose
    var saleStartDateStr: String? = null

    @SerializedName("saleEndDateStr")
    @Expose
    var saleEndDateStr: String? = null
}