package com.example.android.uamp.media.library.nugs.years

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Response {
    @SerializedName("ID")
    @Expose
    var id: Int? = null

    @SerializedName("ownerName")
    @Expose
    var ownerName: String? = null

    @SerializedName("ownerType")
    @Expose
    var ownerType: Int? = null

    @SerializedName("yearListItems")
    @Expose
    var yearListItems: List<String>? = null
}