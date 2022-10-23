package org.igster.bigriver.client.media.library.nugs

import android.support.v4.media.MediaMetadataCompat

interface IApi {
    suspend fun update(mediaId: String, nugs: UpdateNugs)
}

interface UpdateNugs {
    fun setMedia(mediaId: String, media: MutableList<MediaMetadataCompat>)
    fun addTrack(mediaId: String, track: MediaMetadataCompat)
}