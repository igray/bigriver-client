package org.igster.bigriver.client.media.library.nugs.years

import android.content.Context
import android.net.Uri
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import org.igster.bigriver.client.media.R
import org.igster.bigriver.client.media.extensions.albumArtUri
import org.igster.bigriver.client.media.extensions.flag
import org.igster.bigriver.client.media.extensions.id
import org.igster.bigriver.client.media.extensions.title
import org.igster.bigriver.client.media.library.RESOURCE_ROOT_URI
import org.igster.bigriver.client.media.library.nugs.IApi
import org.igster.bigriver.client.media.library.nugs.UpdateNugs
import org.igster.bigriver.client.media.library.nugs.UriBuilder
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL

class Api(val context: Context, val artistId: String): IApi {
    private var uri: Uri

    init {
        val builder = UriBuilder("catalog.artist.years")
        builder["artistId"] = artistId
        builder["limit"] = "100"
        uri = builder.build()
    }

    override suspend fun update(mediaId: String, nugs: UpdateNugs) {
        withContext(Dispatchers.IO) {
            val data = download()
            val mediaMetadataCompats = data.response!!.yearListItems!!
                .filter { year -> year.toIntOrNull() != null }
                .map { year ->
                MediaMetadataCompat.Builder()
                    .fromYear(artistId, year)
                    .apply {
                        albumArtUri = RESOURCE_ROOT_URI +
                                context.resources.getResourceEntryName(R.drawable.ic_artists)
                    }
                    .build()
            }.toList()
            nugs.setMedia(mediaId, mediaMetadataCompats.toMutableList())
        }
    }

    private fun download(): Years {
        val conn = URL(uri.toString())
        val reader = BufferedReader(InputStreamReader(conn.openStream()))
        return Gson().fromJson(reader, Years::class.java)
    }
}

fun MediaMetadataCompat.Builder.fromYear(artistId: String, year: String): MediaMetadataCompat.Builder {
    id = "artistyear_" + artistId + "_" + year
    title = year
    flag = MediaBrowserCompat.MediaItem.FLAG_BROWSABLE

    return this
}
