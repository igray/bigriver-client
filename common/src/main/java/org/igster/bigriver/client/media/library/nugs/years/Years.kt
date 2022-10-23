package org.igster.bigriver.client.media.library.nugs.years

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Years {
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