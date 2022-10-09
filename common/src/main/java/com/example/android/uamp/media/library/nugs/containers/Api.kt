package com.example.android.uamp.media.library.nugs.containers

import android.net.Uri
import com.example.android.uamp.media.library.nugs.UriBuilder
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL

class Api(artistId: String, year: Int) {
    private var uri: Uri

    init {
        val builder = UriBuilder("catalog.containersAll")
        builder["artistList"] = artistId
        builder["startOffset"] = "1"
        builder["showYears"] = year.toString()
        builder["sortType"] = "desc"
        builder["sortBy"] = "performanceDate"
        builder["availType"] = "1"
        builder["limit"] = "400"
        uri = builder.build()
    }

    fun download(): Containers {
        val conn = URL(uri.toString())
        val reader = BufferedReader(InputStreamReader(conn.openStream()))
        return Gson().fromJson(reader, Containers::class.java)
    }
}
