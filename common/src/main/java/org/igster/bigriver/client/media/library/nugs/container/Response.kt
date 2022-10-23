package org.igster.bigriver.client.media.library.nugs.container

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Response {
    @SerializedName("numReviews")
    @Expose
    var numReviews: Int? = null

    @SerializedName("totalContainerRunningTime")
    @Expose
    var totalContainerRunningTime: Int? = null

    @SerializedName("hhmmssTotalRunningTime")
    @Expose
    var hhmmssTotalRunningTime: String? = null

    @SerializedName("products")
    @Expose
    var products: List<Product>? = null

    @SerializedName("subscriptions")
    @Expose
    var subscriptions: Any? = null

    @SerializedName("tracks")
    @Expose
    var tracks: List<Track>? = null

    @SerializedName("pics")
    @Expose
    var pics: List<Pic>? = null

    @SerializedName("recommendations")
    @Expose
    var recommendations: List<Any>? = null

    @SerializedName("reviews")
    @Expose
    var reviews: Reviews? = null

    @SerializedName("notes")
    @Expose
    var notes: List<Note>? = null

    @SerializedName("categoryID")
    @Expose
    var categoryID: Int? = null

    @SerializedName("labels")
    @Expose
    var labels: Any? = null

    @SerializedName("prevContainerID")
    @Expose
    var prevContainerID: Int? = null

    @SerializedName("nextContainerID")
    @Expose
    var nextContainerID: Int? = null

    @SerializedName("prevContainerURL")
    @Expose
    var prevContainerURL: String? = null

    @SerializedName("nextContainerURL")
    @Expose
    var nextContainerURL: String? = null

    @SerializedName("volumeName")
    @Expose
    var volumeName: String? = null

    @SerializedName("cdArtWorkList")
    @Expose
    var cdArtWorkList: List<CdArtWork>? = null

    @SerializedName("containerGroups")
    @Expose
    var containerGroups: Any? = null

    @SerializedName("videoURL")
    @Expose
    var videoURL: Any? = null

    @SerializedName("videoImage")
    @Expose
    var videoImage: Any? = null

    @SerializedName("videoTitle")
    @Expose
    var videoTitle: Any? = null

    @SerializedName("videoDesc")
    @Expose
    var videoDesc: Any? = null

    @SerializedName("vodPlayerImage")
    @Expose
    var vodPlayerImage: String? = null

    @SerializedName("isInSubscriptionProgram")
    @Expose
    var isInSubscriptionProgram: Boolean? = null

    @SerializedName("svodskuID")
    @Expose
    var svodskuID: Int? = null

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