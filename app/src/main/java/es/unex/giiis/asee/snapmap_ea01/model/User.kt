package es.unex.giiis.asee.snapmap_ea01.model

import java.io.Serializable

class User (
    val username: String = "",
    val aboutMe: String = "",
    val email: String = "",
    val password: String = ""

) : Serializable