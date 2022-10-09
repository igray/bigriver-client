package com.example.android.uamp.media.library.nugs.artists

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Response {
    @SerializedName("artists")
    @Expose
    var artists: List<Artist>? = null
}