package com.example.android.uamp.media.library.nugs.container

import android.net.Uri
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
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
import com.example.android.uamp.media.library.JsonSource
import com.example.android.uamp.media.library.nugs.IApi
import com.example.android.uamp.media.library.nugs.UpdateNugs
import com.example.android.uamp.media.library.nugs.UriBuilder
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.util.concurrent.TimeUnit

class Api(val containerId: String): IApi {
    private var uri: Uri

    init {
        val builder = UriBuilder("catalog.container")
        builder["containerID"] = containerId
        builder["vdisp"] = "1"
        uri = builder.build()
    }

    override suspend fun update(mediaId: String, nugs: UpdateNugs) {
        return withContext(Dispatchers.IO) {
            val data = download()
            val mediaMetadataCompats = data.response!!.tracks!!.map { track ->
                val jsonImageUri = Uri.parse("https://secure.livedownloads.com" + data.response!!.img?.url)
                val imageUri = AlbumArtContentProvider.mapUri(jsonImageUri)
                val media = MediaMetadataCompat.Builder()
                    .fromTrack(data.response!!, track)
                    .apply {
                        displayIconUri = imageUri.toString() // Used by ExoPlayer and Notification
                        albumArtUri = imageUri.toString()
                        // Keep the original artwork URI for being included in Cast metadata object.
                        putString(JsonSource.ORIGINAL_ARTWORK_URI_KEY, jsonImageUri.toString())
                    }
                    .build()
                nugs.addTrack(media.id!!, media)
                media
            }.toList()
            nugs.setMedia(mediaId, mediaMetadataCompats.toMutableList())
        }
    }

    private fun download(): Container {
        val conn = URL(uri.toString())
        val reader = BufferedReader(InputStreamReader(conn.openStream()))
        return Gson().fromJson(reader, Container::class.java)
    }
}

fun MediaMetadataCompat.Builder.fromTrack(response: Response, track: Track): MediaMetadataCompat.Builder {
    val durationMs = TimeUnit.SECONDS.toMillis(track.totalRunningTime!!.toLong())

    id = "track_" + track.trackID
    title = track.songTitle
    artist = response.artistName
    album = response.containerInfo
    duration = durationMs
    genre = response.artistName
    mediaUri = track.clipURL
    trackNumber = track.trackNum!!.toLong()
    trackCount = response.tracks!!.size.toLong()
    flag = MediaBrowserCompat.MediaItem.FLAG_PLAYABLE

    // To make things easier for *displaying* these, set the display properties as well.
    displayTitle = track.songTitle
    displaySubtitle = response.artistName
    displayDescription = response.containerInfo
    displayIconUri = "https://secure.livedownloads.com" + response.img?.url

    // Add downloadStatus to force the creation of an "extras" bundle in the resulting
    // MediaMetadataCompat object. This is needed to send accurate metadata to the
    // media session during updates.
    downloadStatus = MediaDescriptionCompat.STATUS_NOT_DOWNLOADED

    // Allow it to be used in the typical builder style.
    return this
}