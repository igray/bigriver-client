package org.igster.bigriver.client.media.library.nugs.container

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CdArtWork {
    @SerializedName("discNumber")
    @Expose
    var discNumber: Int? = null

    @SerializedName("artWorkType")
    @Expose
    var artWorkType: Int? = null

    @SerializedName("artWorkTypeStr")
    @Expose
    var artWorkTypeStr: String? = null

    @SerializedName("templateType")
    @Expose
    var templateType: Int? = null

    @SerializedName("artWorkPath")
    @Expose
    var artWorkPath: String? = null
}