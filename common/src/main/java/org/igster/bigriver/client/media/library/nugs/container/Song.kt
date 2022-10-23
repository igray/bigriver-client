package org.igster.bigriver.client.media.library.nugs.container

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Song {
    @SerializedName("songID")
    @Expose
    var songID: Int? = null

    @SerializedName("songTitle")
    @Expose
    var songTitle: String? = null

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
}