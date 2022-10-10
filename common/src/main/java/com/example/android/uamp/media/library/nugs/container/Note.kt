package com.example.android.uamp.media.library.nugs.container

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Note {
    @SerializedName("noteID")
    @Expose
    var noteID: Int? = null

    @SerializedName("note")
    @Expose
    var note: String? = null
}