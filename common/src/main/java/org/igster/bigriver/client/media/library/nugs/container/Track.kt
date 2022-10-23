package org.igster.bigriver.client.media.library.nugs.container

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Track {
    @SerializedName("accessList")
    @Expose
    var accessList: List<Any>? = null

    @SerializedName("hhmmssTotalRunningTime")
    @Expose
    var hhmmssTotalRunningTime: String? = null

    @SerializedName("trackLabel")
    @Expose
    var trackLabel: String? = null

    @SerializedName("trackURL")
    @Expose
    var trackURL: String? = null

    @SerializedName("songID")
    @Expose
    var songID: Int? = null

    @SerializedName("songTitle")
    @Expose
    var songTitle: String? = null

    @SerializedName("totalRunningTime")
    @Expose
    var totalRunningTime: Int? = null

    @SerializedName("discNum")
    @Expose
    var discNum: Int? = null

    @SerializedName("trackNum")
    @Expose
    var trackNum: Int? = null

    @SerializedName("setNum")
    @Expose
    var setNum: Int? = null

    @SerializedName("clipURL")
    @Expose
    var clipURL: String? = null

    @SerializedName("trackID")
    @Expose
    var trackID: Int? = null

    @SerializedName("trackExclude")
    @Expose
    var trackExclude: Int? = null

    @SerializedName("rootpath")
    @Expose
    var rootpath: Any? = null

    @SerializedName("sourcePath")
    @Expose
    var sourcePath: Any? = null

    @SerializedName("sourceFilename")
    @Expose
    var sourceFilename: Any? = null

    @SerializedName("sourceFilePath")
    @Expose
    var sourceFilePath: Any? = null

    @SerializedName("rootPathReal")
    @Expose
    var rootPathReal: Any? = null

    @SerializedName("sourceFilePathReal")
    @Expose
    var sourceFilePathReal: Any? = null

    @SerializedName("skuIDExt")
    @Expose
    var skuIDExt: Any? = null

    @SerializedName("transportMethod")
    @Expose
    var transportMethod: String? = null

    @SerializedName("strTotalRunningTime")
    @Expose
    var strTotalRunningTime: Any? = null

    @SerializedName("products")
    @Expose
    var products: List<Any>? = null

    @SerializedName("subscriptions")
    @Expose
    var subscriptions: Any? = null

    @SerializedName("audioProduct")
    @Expose
    var audioProduct: Any? = null

    @SerializedName("audioLosslessProduct")
    @Expose
    var audioLosslessProduct: Any? = null

    @SerializedName("audioHDProduct")
    @Expose
    var audioHDProduct: Any? = null

    @SerializedName("videoProduct")
    @Expose
    var videoProduct: Any? = null

    @SerializedName("livestreamProduct")
    @Expose
    var livestreamProduct: Any? = null

    @SerializedName("mp4Product")
    @Expose
    var mp4Product: Any? = null

    @SerializedName("videoondemandProduct")
    @Expose
    var videoondemandProduct: Any? = null

    @SerializedName("cdProduct")
    @Expose
    var cdProduct: Any? = null

    @SerializedName("liveHDstreamProduct")
    @Expose
    var liveHDstreamProduct: Any? = null

    @SerializedName("HDvideoondemandProduct")
    @Expose
    var hDvideoondemandProduct: Any? = null

    @SerializedName("vinylProduct")
    @Expose
    var vinylProduct: Any? = null

    @SerializedName("dsdProduct")
    @Expose
    var dsdProduct: Any? = null

    @SerializedName("dvdProduct")
    @Expose
    var dvdProduct: Any? = null

    @SerializedName("reality360Product")
    @Expose
    var reality360Product: Any? = null

    @SerializedName("containerGroups")
    @Expose
    var containerGroups: Any? = null

    @SerializedName("IDList")
    @Expose
    var iDList: String? = null

    @SerializedName("playListID")
    @Expose
    var playListID: Int? = null

    @SerializedName("catalogQueryType")
    @Expose
    var catalogQueryType: Int? = null
}