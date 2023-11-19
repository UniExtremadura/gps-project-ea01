package es.unex.giiis.asee.snapmap_ea01.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

//User with setters and getters
@Entity
data class User (
    @PrimaryKey(autoGenerate = true) var userId: Long?,
    var username: String = "",
    var aboutMe: String = "",
    var email: String = "",
    var password: String = ""
) : Serializable