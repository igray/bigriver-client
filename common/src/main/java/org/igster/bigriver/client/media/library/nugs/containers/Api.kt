package org.igster.bigriver.client.media.library.nugs.containers

import android.net.Uri
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import org.igster.bigriver.client.media.extensions.albumArtUri
import org.igster.bigriver.client.media.extensions.artist
import org.igster.bigriver.client.media.extensions.displayIconUri
import org.igster.bigriver.client.media.extensions.flag
import org.igster.bigriver.client.media.extensions.id
import org.igster.bigriver.client.media.extensions.title
import org.igster.bigriver.client.media.library.AlbumArtContentProvider
import org.igster.bigriver.client.media.library.JsonSource
import org.igster.bigriver.client.media.library.nugs.IApi
import org.igster.bigriver.client.media.library.nugs.UpdateNugs
import org.igster.bigriver.client.media.library.nugs.UriBuilder
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
                val jsonImageUri = Uri.parse("https://secure.livedownloads.com" + container.img?.url)
                val imageUri = AlbumArtContentProvider.mapUri(jsonImageUri)
                MediaMetadataCompat.Builder()
                    .fromContainer(container)
                    .apply {
                        displayIconUri = imageUri.toString() // Used by ExoPlayer and Notification
                        albumArtUri = imageUri.toString()
                        // Keep the original artwork URI for being included in Cast metadata object.
                        putString(JsonSource.ORIGINAL_ARTWORK_URI_KEY, jsonImageUri.toString())
                    }
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
    flag = MediaBrowserCompat.MediaItem.FLAG_BROWSABLE

    return this
}