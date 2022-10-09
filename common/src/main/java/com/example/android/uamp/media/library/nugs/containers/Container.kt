package com.example.android.uamp.media.library.nugs.containers

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Container {
    @SerializedName("licensorName")
    @Expose
    var licensorName: String? = null

    @SerializedName("affID")
    @Expose
    var affID: Int? = null

    @SerializedName("pageURL")
    @Expose
    var pageURL: String? = null

    @SerializedName("coverImage")
    @Expose
    var coverImage: Any? = null

    @SerializedName("venueName")
    @Expose
    var venueName: String? = null

    @SerializedName("venueCity")
    @Expose
    var venueCity: String? = null

    @SerializedName("venueState")
    @Expose
    var venueState: String? = null

    @SerializedName("artistName")
    @Expose
    var artistName: String? = null

    @SerializedName("accessList")
    @Expose
    var accessList: List<Any>? = null

    @SerializedName("availabilityType")
    @Expose
    var availabilityType: Int? = null

    @SerializedName("availabilityTypeStr")
    @Expose
    var availabilityTypeStr: String? = null

    @SerializedName("venue")
    @Expose
    var venue: String? = null

    @SerializedName("img")
    @Expose
    var img: Img? = null

    @SerializedName("containerID")
    @Expose
    var containerID: Int? = null

    @SerializedName("containerInfo")
    @Expose
    var containerInfo: String? = null

    @SerializedName("performanceDate")
    @Expose
    var performanceDate: String? = null

    @SerializedName("performanceDateFormatted")
    @Expose
    var performanceDateFormatted: String? = null

    @SerializedName("performanceDateYear")
    @Expose
    var performanceDateYear: String? = null

    @SerializedName("performanceDateShort")
    @Expose
    var performanceDateShort: String? = null

    @SerializedName("performanceDateShortYearFirst")
    @Expose
    var performanceDateShortYearFirst: String? = null

    @SerializedName("performanceDateAbbr")
    @Expose
    var performanceDateAbbr: String? = null

    @SerializedName("songList")
    @Expose
    var songList: Any? = null

    @SerializedName("releaseDate")
    @Expose
    var releaseDate: Any? = null

    @SerializedName("releaseDateFormatted")
    @Expose
    var releaseDateFormatted: String? = null

    @SerializedName("activeState")
    @Expose
    var activeState: String? = null

    @SerializedName("containerType")
    @Expose
    var containerType: Int? = null

    @SerializedName("containerTypeStr")
    @Expose
    var containerTypeStr: String? = null

    @SerializedName("songs")
    @Expose
    var songs: List<Song>? = null

    @SerializedName("salesLast30")
    @Expose
    var salesLast30: Int? = null

    @SerializedName("salesAllTime")
    @Expose
    var salesAllTime: Int? = null

    @SerializedName("dateCreated")
    @Expose
    var dateCreated: String? = null

    @SerializedName("epochDateCreated")
    @Expose
    var epochDateCreated: Int? = null

    @SerializedName("productFormatList")
    @Expose
    var productFormatList: List<ProductFormat>? = null

    @SerializedName("containsPreviewVideo")
    @Expose
    var containsPreviewVideo: Int? = null

    @SerializedName("artistID")
    @Expose
    var artistID: Int? = null

    @SerializedName("containerCategoryID")
    @Expose
    var containerCategoryID: Int? = null

    @SerializedName("containerCategoryName")
    @Expose
    var containerCategoryName: Any? = null

    @SerializedName("containerCode")
    @Expose
    var containerCode: String? = null

    @SerializedName("containerIDExt")
    @Expose
    var containerIDExt: Any? = null

    @SerializedName("extImage")
    @Expose
    var extImage: String? = null

    @SerializedName("videoChapters")
    @Expose
    var videoChapters: Any? = null

    @SerializedName("hasSony360VOD")
    @Expose
    var hasSony360VOD: Boolean? = null
}