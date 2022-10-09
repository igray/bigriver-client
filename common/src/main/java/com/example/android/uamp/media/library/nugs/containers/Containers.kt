package com.example.android.uamp.media.library.nugs.containers

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Containers {
    @SerializedName("methodName")
    @Expose
    var methodName: String? = null

    @SerializedName("responseAvailabilityCode")
    @Expose
    var responseAvailabilityCode: Int? = null

    @SerializedName("responseAvailabilityCodeStr")
    @Expose
    var responseAvailabilityCodeStr: String? = null

    @SerializedName("Response")
    @Expose
    var response: Response? = null
}