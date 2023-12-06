package es.unex.giiis.asee.snapmap_ea01.data

import es.unex.giiis.asee.snapmap_ea01.data.api.PhotoURI
import es.unex.giiis.asee.snapmap_ea01.data.model.PhotoURI_DB

fun PhotoURI.toPhoto() = PhotoURI_DB(
    photoURI = id ?: 0,
    uri = uri ?: ""
)