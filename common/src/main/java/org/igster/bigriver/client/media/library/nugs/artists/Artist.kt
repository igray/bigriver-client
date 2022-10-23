package org.igster.bigriver.client.media.library.nugs.artists

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Artist {
    @SerializedName("artistImage")
    @Expose
    var artistImage: String? = null

    @SerializedName("artistID")
    @Expose
    var artistID: Int? = null

    @SerializedName("artistName")
    @Expose
    var artistName: String? = null

    @SerializedName("artistNameNoThe")
    @Expose
    var artistNameNoThe: String? = null

    @SerializedName("numShows")
    @Expose
    var numShows: Int? = null

    @SerializedName("numAlbums")
    @Expose
    var numAlbums: Int? = null

    @SerializedName("pageURL")
    @Expose
    var pageURL: String? = null
}