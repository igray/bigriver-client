package com.example.android.uamp.media.library.nugs.years

import android.net.Uri
import com.example.android.uamp.media.library.nugs.UriBuilder
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL

class Api(artistId: String) {
    private var uri: Uri

    init {
        val builder = UriBuilder("catalog.artist.years")
        builder["artistId"] = artistId
        builder["limit"] = "100"
        uri = builder.build()
    }

    fun download(): Years {
        val conn = URL(uri.toString())
        val reader = BufferedReader(InputStreamReader(conn.openStream()))
        return Gson().fromJson(reader, Years::class.java)
    }
}
