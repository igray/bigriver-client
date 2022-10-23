package org.igster.bigriver.client.media.library

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.util.Log
import org.igster.bigriver.client.media.R
import org.igster.bigriver.client.media.extensions.album
import org.igster.bigriver.client.media.extensions.albumArtUri
import org.igster.bigriver.client.media.extensions.albumArtist
import org.igster.bigriver.client.media.extensions.artist
import org.igster.bigriver.client.media.extensions.containsCaseInsensitive
import org.igster.bigriver.client.media.extensions.flag
import org.igster.bigriver.client.media.extensions.genre
import org.igster.bigriver.client.media.extensions.id
import org.igster.bigriver.client.media.extensions.title
import org.igster.bigriver.client.media.library.nugs.UpdateNugs
import org.igster.bigriver.client.media.library.nugs.artists.Api as ArtistsApi
import org.igster.bigriver.client.media.library.nugs.container.Api as ContainerApi
import org.igster.bigriver.client.media.library.nugs.containers.Api as ContainersApi
import org.igster.bigriver.client.media.library.nugs.years.Api as YearsApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.IOException

/**
 * Source of [MediaMetadataCompat] objects created from a basic JSON stream.
 *
 * The definition of the JSON is specified in the docs of [JsonMusic] in this file,
 * which is the object representation of it.
 */
class NugsSource(val context: Context, private val serviceScope: CoroutineScope): Iterable<MediaMetadataCompat>,
    UpdateNugs {
    private val mediaIdToChildren = mutableMapOf<String, MutableList<MediaMetadataCompat>>()
    private val mediaStatus = mutableMapOf<String, Pair<Int, MutableList<(Boolean) -> Unit>>>()
    private val tracks = mutableMapOf<String, MediaMetadataCompat>()

    override fun iterator(): MutableIterator<MediaMetadataCompat> = tracks.values.iterator()

    init {
        val rootList: MutableList<MediaMetadataCompat> = mutableListOf()
        val artistsMetadata = MediaMetadataCompat.Builder().apply {
            id = UAMP_ARTISTS_ROOT
            title = context.getString(R.string.artists_title)
            albumArtUri = RESOURCE_ROOT_URI +
                    context.resources.getResourceEntryName(R.drawable.ic_artists)
            flag = MediaBrowserCompat.MediaItem.FLAG_BROWSABLE
        }.build()

        rootList += artistsMetadata
        mediaIdToChildren[UAMP_BROWSABLE_ROOT] = rootList
    }

    fun whenReady(mediaId: String, performAction: (Boolean) -> Unit): Boolean {
        if (mediaIdToChildren.containsKey(mediaId) || tracks.containsKey(mediaId)) {
            performAction(true)
            return true
        }

        val status = mediaStatus[mediaId]
        if (status == null) {
            mediaStatus[mediaId] = Pair(STATE_CREATED, mutableListOf(performAction))
            serviceScope.launch { load(mediaId) }
            return false
        } else {
            return when (status.first) {
                STATE_CREATED, STATE_INITIALIZING -> {
                    status.second += performAction
                    false
                }
                else -> {
                    performAction(status.first != STATE_ERROR)
                    true
                }
            }
        }
    }

    fun search(query: String, extras: Bundle): List<MediaMetadataCompat> {
        // First attempt to search with the "focus" that's provided in the extras.
        val focusSearchResult = when (extras[MediaStore.EXTRA_MEDIA_FOCUS]) {
            MediaStore.Audio.Genres.ENTRY_CONTENT_TYPE -> {
                // For a Genre focused search, only genre is set.
                val genre = extras[EXTRA_MEDIA_GENRE]
                Log.d(TAG, "Focused genre search: '$genre'")
                filter { song ->
                    song.genre == genre
                }
            }
            MediaStore.Audio.Artists.ENTRY_CONTENT_TYPE -> {
                // For an Artist focused search, only the artist is set.
                val artist = extras[MediaStore.EXTRA_MEDIA_ARTIST]
                Log.d(TAG, "Focused artist search: '$artist'")
                filter { song ->
                    (song.artist == artist || song.albumArtist == artist)
                }
            }
            MediaStore.Audio.Albums.ENTRY_CONTENT_TYPE -> {
                // For an Album focused search, album and artist are set.
                val artist = extras[MediaStore.EXTRA_MEDIA_ARTIST]
                val album = extras[MediaStore.EXTRA_MEDIA_ALBUM]
                Log.d(TAG, "Focused album search: album='$album' artist='$artist")
                filter { song ->
                    (song.artist == artist || song.albumArtist == artist) && song.album == album
                }
            }
            MediaStore.Audio.Media.ENTRY_CONTENT_TYPE -> {
                // For a Song (aka Media) focused search, title, album, and artist are set.
                val title = extras[MediaStore.EXTRA_MEDIA_TITLE]
                val album = extras[MediaStore.EXTRA_MEDIA_ALBUM]
                val artist = extras[MediaStore.EXTRA_MEDIA_ARTIST]
                Log.d(TAG, "Focused media search: title='$title' album='$album' artist='$artist")
                filter { song ->
                    (song.artist == artist || song.albumArtist == artist) && song.album == album
                            && song.title == title
                }
            }
            else -> {
                // There isn't a focus, so no results yet.
                emptyList()
            }
        }

        // If there weren't any results from the focused search (or if there wasn't a focus
        // to begin with), try to find any matches given the 'query' provided, searching against
        // a few of the fields.
        // In this sample, we're just checking a few fields with the provided query, but in a
        // more complex app, more logic could be used to find fuzzy matches, etc...
        if (focusSearchResult.isEmpty()) {
            return if (query.isNotBlank()) {
                Log.d(TAG, "Unfocused search for '$query'")
                filter { song ->
                    song.title.containsCaseInsensitive(query)
                            || song.genre.containsCaseInsensitive(query)
                }
            } else {
                // If the user asked to "play music", or something similar, the query will also
                // be blank. Given the small catalog of songs in the sample, just return them
                // all, shuffled, as something to play.
                Log.d(TAG, "Unfocused search without keyword")
                return shuffled()
            }
        } else {
            return focusSearchResult
        }
    }

    operator fun get(mediaId: String) = mediaIdToChildren[mediaId]

    private suspend fun load(mediaId: String) {
        updateMediaState(mediaId, STATE_INITIALIZING)
        val loader = if(mediaId == UAMP_ARTISTS_ROOT) ArtistsApi(context)
        else if(mediaId.startsWith("artist_")) {
            YearsApi(context, mediaId.replace("artist_", ""))
        }
        else if(mediaId.startsWith("artistyear_")) {
            val parts = mediaId.split("_")
            ContainersApi(parts[1], parts[2].toInt())
        }
        else if(mediaId.startsWith("container_")) {
            ContainerApi(mediaId.replace("container_", ""))
        }
        else return

        try {
            loader.update(mediaId, this)
            updateMediaState(mediaId, STATE_INITIALIZED)
        } catch (ioException: IOException) {
            updateMediaState(mediaId, STATE_ERROR)
        }
    }

    override fun setMedia(mediaId: String, media: MutableList<MediaMetadataCompat>) {
        mediaIdToChildren[mediaId] = media
    }

    override fun addTrack(mediaId: String, track: MediaMetadataCompat) {
        tracks[mediaId] = track
    }

    private fun updateMediaState(mediaId: String, status: Int) {
        val pair = mediaStatus[mediaId]!!
        if (status == STATE_INITIALIZED || status == STATE_ERROR) {
            synchronized(pair.second) {
                pair.second.forEach { listener ->
                    listener(status == STATE_INITIALIZED)
                }
            }
            mediaStatus[mediaId] = Pair(status, mutableListOf())
        } else {
            mediaStatus[mediaId] = pair.copy(first = status)
        }
    }

    /**
     * [MediaStore.EXTRA_MEDIA_GENRE] is missing on API 19. Hide this fact by using our
     * own version of it.
     */
    private val EXTRA_MEDIA_GENRE
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            MediaStore.EXTRA_MEDIA_GENRE
        } else {
            "android.intent.extra.genre"
        }
}

const val UAMP_ARTISTS_ROOT = "__ALBUMS__"
const val UAMP_SEARCH_KEY = "__SEARCH__"
private const val TAG = "MusicSource"