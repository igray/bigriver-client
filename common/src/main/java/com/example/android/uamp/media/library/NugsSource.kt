package com.example.android.uamp.media.library

import android.content.Context
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import com.example.android.uamp.media.R
import com.example.android.uamp.media.extensions.albumArtUri
import com.example.android.uamp.media.extensions.flag
import com.example.android.uamp.media.extensions.id
import com.example.android.uamp.media.extensions.title
import com.example.android.uamp.media.library.nugs.artists.Api as ArtistsApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Source of [MediaMetadataCompat] objects created from a basic JSON stream.
 *
 * The definition of the JSON is specified in the docs of [JsonMusic] in this file,
 * which is the object representation of it.
 */
class NugsSource(val context: Context, private val serviceScope: CoroutineScope): Iterable<MediaMetadataCompat> {
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
        if (mediaIdToChildren.containsKey(mediaId)) {
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
       return mutableListOf()
    }

    operator fun get(mediaId: String) = mediaIdToChildren[mediaId]

    private suspend fun load(mediaId: String) {
        updateMediaState(mediaId, STATE_INITIALIZING)
        when(mediaId) {
            UAMP_ARTISTS_ROOT -> {
                ArtistsApi(context).load()?.let { artists ->
                    mediaIdToChildren[mediaId] = artists.toMutableList()
                    updateMediaState(mediaId, STATE_INITIALIZED)
                } ?: run {
                    updateMediaState(mediaId, STATE_ERROR)
                }
            }
        }
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
}

const val UAMP_ARTISTS_ROOT = "__ALBUMS__"
const val UAMP_SEARCH_KEY = "__SEARCH__"

/**
 * Extension method for [MediaMetadataCompat.Builder] to set the fields from
 * our JSON constructed object (to make the code a bit easier to see).
 */