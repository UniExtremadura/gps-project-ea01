package es.unex.giiis.asee.snapmap_ea01.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
class PhotoURI_DB (
    @PrimaryKey val photoURI: Long,
    val uri: String
):Serializable