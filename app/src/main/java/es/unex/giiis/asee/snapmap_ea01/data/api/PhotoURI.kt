package es.unex.giiis.asee.snapmap_ea01.data.api

import com.google.gson.annotations.SerializedName

data class PhotoURI(

    @SerializedName("id") var id: Long? = null,
    @SerializedName("download_url") var uri: String? = null
)