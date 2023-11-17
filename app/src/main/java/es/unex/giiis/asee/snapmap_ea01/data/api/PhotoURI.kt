package es.unex.giiis.asee.snapmap_ea01.data.api

import com.google.gson.annotations.SerializedName

data class PhotoURI(
    @SerializedName("message") var uri: String? = null
)