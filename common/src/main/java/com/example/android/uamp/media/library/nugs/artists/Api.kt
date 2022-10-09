package com.example.android.uamp.media.library.nugs.artists

import android.content.Context
import android.net.Uri
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import com.example.android.uamp.media.R
import com.example.android.uamp.media.extensions.album
import com.example.android.uamp.media.extensions.albumArtUri
import com.example.android.uamp.media.extensions.artist
import com.example.android.uamp.media.extensions.displayDescription
import com.example.android.uamp.media.extensions.displayIconUri
import com.example.android.uamp.media.extensions.displaySubtitle
import com.example.android.uamp.media.extensions.displayTitle
import com.example.android.uamp.media.extensions.downloadStatus
import com.example.android.uamp.media.extensions.duration
import com.example.android.uamp.media.extensions.flag
import com.example.android.uamp.media.extensions.genre
import com.example.android.uamp.media.extensions.id
import com.example.android.uamp.media.extensions.mediaUri
import com.example.android.uamp.media.extensions.title
import com.example.android.uamp.media.extensions.trackCount
import com.example.android.uamp.media.extensions.trackNumber
import com.example.android.uamp.media.library.AlbumArtContentProvider
import com.example.android.uamp.media.library.JsonMusic
import com.example.android.uamp.media.library.RESOURCE_ROOT_URI
import com.example.android.uamp.media.library.UAMP_ARTISTS_ROOT
import com.example.android.uamp.media.library.from
import com.example.android.uamp.media.library.nugs.UriBuilder
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import java.util.concurrent.TimeUnit

class Api(val context: Context) {
    private var uri: Uri = UriBuilder("catalog.artists").build()

    suspend fun load(): List<MediaMetadataCompat>? {
        return withContext(Dispatchers.IO) {
            val data = try {
                download()
            } catch (ioException: IOException) {
                return@withContext null
            }
            val mediaMetadataCompats = data.response!!.artists!!.map { artist ->
                MediaMetadataCompat.Builder()
                    .from(artist)
                    .apply {
                        albumArtUri = RESOURCE_ROOT_URI +
                                context.resources.getResourceEntryName(R.drawable.ic_artists)
                    }
                    .build()
            }.toList()
            mediaMetadataCompats
        }
    }

    private fun download(): Artists {
        val conn = URL(uri.toString())
        val reader = BufferedReader(InputStreamReader(conn.openStream()))
        return Gson().fromJson(reader, Artists::class.java)
    }
}

fun MediaMetadataCompat.Builder.from(artist: Artist): MediaMetadataCompat.Builder {
    id = "artist_" + artist.artistID
    title = artist.artistName
    flag = MediaBrowserCompat.MediaItem.FLAG_BROWSABLE

    return this
}
