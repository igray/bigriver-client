package org.igster.bigriver.client.media.library.nugs.artists

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

open class Api(val context: Context): IApi {
    private var uri: Uri = UriBuilder("catalog.artists").build()

    override suspend fun update(mediaId: String, nugs: UpdateNugs) {
        return withContext(Dispatchers.IO) {
            val data = download()
            val mediaMetadataCompats = data.response!!.artists!!.map { artist ->
                MediaMetadataCompat.Builder()
                    .fromArtist(artist)
                    .apply {
                        albumArtUri = RESOURCE_ROOT_URI +
                                context.resources.getResourceEntryName(R.drawable.ic_artists)
                    }
                    .build()
            }.toList()
            nugs.setMedia(mediaId, mediaMetadataCompats.toMutableList())
        }
    }

    private fun download(): Artists {
        val conn = URL(uri.toString())
        val reader = BufferedReader(InputStreamReader(conn.openStream()))
        return Gson().fromJson(reader, Artists::class.java)
    }
}

fun MediaMetadataCompat.Builder.fromArtist(artist: Artist): MediaMetadataCompat.Builder {
    id = "artist_" + artist.artistID
    title = artist.artistName
    flag = MediaBrowserCompat.MediaItem.FLAG_BROWSABLE

    return this
}
