package com.example.android.uamp.media.library.nugs.container

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Item {
    @SerializedName("reviewStatus")
    @Expose
    var reviewStatus: Int? = null

    @SerializedName("reviewStatusStr")
    @Expose
    var reviewStatusStr: String? = null

    @SerializedName("containerID")
    @Expose
    var containerID: Int? = null

    @SerializedName("reviewID")
    @Expose
    var reviewID: Int? = null

    @SerializedName("reviewerName")
    @Expose
    var reviewerName: String? = null

    @SerializedName("reviewDate")
    @Expose
    var reviewDate: String? = null

    @SerializedName("review")
    @Expose
    var review: String? = null
}