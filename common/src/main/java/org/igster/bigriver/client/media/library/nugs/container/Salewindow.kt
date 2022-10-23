package org.igster.bigriver.client.media.library.nugs.container

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Salewindow {
    @SerializedName("sswID")
    @Expose
    var sswID: Int? = null

    @SerializedName("timeZoneToDisplay")
    @Expose
    var timeZoneToDisplay: Any? = null

    @SerializedName("offsetFromLocalTimeToDisplay")
    @Expose
    var offsetFromLocalTimeToDisplay: Int? = null

    @SerializedName("saleStartDateStr")
    @Expose
    var saleStartDateStr: Any? = null

    @SerializedName("saleEndDateStr")
    @Expose
    var saleEndDateStr: Any? = null
}