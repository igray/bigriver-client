package org.igster.bigriver.client.media.library.nugs.container

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LiveEvent {
    @SerializedName("eventID")
    @Expose
    var eventID: Int? = null

    @SerializedName("eventStartDateStr")
    @Expose
    var eventStartDateStr: Any? = null

    @SerializedName("eventEndDateStr")
    @Expose
    var eventEndDateStr: Any? = null

    @SerializedName("timeZoneToDisplay")
    @Expose
    var timeZoneToDisplay: Any? = null

    @SerializedName("offsetFromLocalTimeToDisplay")
    @Expose
    var offsetFromLocalTimeToDisplay: Int? = null

    @SerializedName("UTCoffset")
    @Expose
    var uTCoffset: Int? = null

    @SerializedName("eventCode")
    @Expose
    var eventCode: Any? = null

    @SerializedName("linkType")
    @Expose
    var linkType: Int? = null
}