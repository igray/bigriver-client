package org.igster.bigriver.client.media.library.nugs

import android.net.Uri
import android.net.Uri.Builder

internal class UriBuilder(val method: String) {
    companion object {
        const val STREAM_API_SCHEME = "https"
        const val STREAM_API_AUTHORITY = "streamapi.nugs.net"
        const val STREAM_API_PATH = "api.aspx"
    }
    private var builder: Builder = Builder()

    init {
        builder.scheme(STREAM_API_SCHEME)
            .authority(STREAM_API_AUTHORITY)
            .path(STREAM_API_PATH)
            .appendQueryParameter("method", method)
    }

    operator fun set(key: String, value: String) {
        builder.appendQueryParameter(key, value)
    }

    fun build(): Uri {
        return builder.build()
    }
}