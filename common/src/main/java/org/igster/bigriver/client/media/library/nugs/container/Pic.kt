package org.igster.bigriver.client.media.library.nugs.container

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Pic {
    @SerializedName("picID")
    @Expose
    var picID: Int? = null

    @SerializedName("orderID")
    @Expose
    var orderID: Int? = null

    @SerializedName("height")
    @Expose
    var height: Int? = null

    @SerializedName("width")
    @Expose
    var width: Int? = null

    @SerializedName("caption")
    @Expose
    var caption: String? = null

    @SerializedName("url")
    @Expose
    var url: String? = null
}