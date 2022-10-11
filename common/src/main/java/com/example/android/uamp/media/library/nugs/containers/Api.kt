package com.example.android.uamp.media.library.nugs.containers

import android.net.Uri
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import com.example.android.uamp.media.extensions.albumArtUri
import com.example.android.uamp.media.extensions.artist
import com.example.android.uamp.media.extensions.flag
import com.example.android.uamp.media.extensions.id
import com.example.android.uamp.media.extensions.title
import com.example.android.uamp.media.library.nugs.IApi
import com.example.android.uamp.media.library.nugs.UpdateNugs
import com.example.android.uamp.media.library.nugs.UriBuilder
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL

class Api(artistId: String, year: Int): IApi {
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

    override suspend fun update(mediaId: String, nugs: UpdateNugs) {
        return withContext(Dispatchers.IO) {
            val data = download()
            val mediaMetadataCompats = data.response!!.containers!!.map { container ->
                MediaMetadataCompat.Builder()
                    .fromContainer(container)
                    .build()
            }.toList()
            nugs.setMedia(mediaId, mediaMetadataCompats.toMutableList())
        }
    }

    private fun download(): Containers {
        val conn = URL(uri.toString())
        val reader = BufferedReader(InputStreamReader(conn.openStream()))
        return Gson().fromJson(reader, Containers::class.java)
    }
}

fun MediaMetadataCompat.Builder.fromContainer(container: Container): MediaMetadataCompat.Builder {
    id = "container_" + container.containerID
    title = container.performanceDate
    artist = container.venue
    albumArtUri = "https://secure.livedownloads.com" + container.img?.url
    flag = MediaBrowserCompat.MediaItem.FLAG_BROWSABLE

    return this
}