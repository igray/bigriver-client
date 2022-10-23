package org.igster.bigriver.client.media.library.nugs.container

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Reviews {
    @SerializedName("containerID")
    @Expose
    var containerID: Int? = null

    @SerializedName("items")
    @Expose
    var items: List<Item>? = null

    @SerializedName("isMoreRecords")
    @Expose
    var isMoreRecords: Boolean? = null

    @SerializedName("totalPages")
    @Expose
    var totalPages: Int? = null

    @SerializedName("totalRecords")
    @Expose
    var totalRecords: Int? = null

    @SerializedName("numPerPage")
    @Expose
    var numPerPage: Int? = null

    @SerializedName("pageNum")
    @Expose
    var pageNum: Int? = null
}