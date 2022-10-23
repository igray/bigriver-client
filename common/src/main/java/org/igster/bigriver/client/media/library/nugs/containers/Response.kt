package org.igster.bigriver.client.media.library.nugs.containers

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Response {
    @SerializedName("headerName")
    @Expose
    var headerName: Any? = null

    @SerializedName("packages")
    @Expose
    var packages: Any? = null

    @SerializedName("containers")
    @Expose
    var containers: List<Container>? = null

    @SerializedName("categoryID")
    @Expose
    var categoryID: Int? = null

    @SerializedName("artistID")
    @Expose
    var artistID: Int? = null

    @SerializedName("artistName")
    @Expose
    var artistName: Any? = null

    @SerializedName("loadingState")
    @Expose
    var loadingState: Int? = null

    @SerializedName("totalMatchedRecords")
    @Expose
    var totalMatchedRecords: Int? = null

    @SerializedName("nnCheckSum")
    @Expose
    var nnCheckSum: Int? = null
}