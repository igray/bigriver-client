package com.example.android.uamp.media.library.nugs.containers

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LiveEvent {
    @SerializedName("eventID")
    @Expose
    var eventID: Int? = null

    @SerializedName("eventStartDateStr")
    @Expose
    var eventStartDateStr: String? = null

    @SerializedName("eventEndDateStr")
    @Expose
    var eventEndDateStr: String? = null

    @SerializedName("timeZoneToDisplay")
    @Expose
    var timeZoneToDisplay: String? = null

    @SerializedName("offsetFromLocalTimeToDisplay")
    @Expose
    var offsetFromLocalTimeToDisplay: Int? = null

    @SerializedName("UTCoffset")
    @Expose
    var uTCoffset: Int? = null

    @SerializedName("eventCode")
    @Expose
    var eventCode: String? = null

    @SerializedName("linkType")
    @Expose
    var linkType: Int? = null
}