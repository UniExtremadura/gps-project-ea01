package es.unex.giiis.asee.snapmap_ea01.data.model

data class Photo(
    val photoURL: String = "",
    val lat: Double,
    val lon: Double,
    val owner: Long?
)
