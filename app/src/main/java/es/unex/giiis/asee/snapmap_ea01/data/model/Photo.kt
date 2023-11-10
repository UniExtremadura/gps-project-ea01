package es.unex.giiis.asee.snapmap_ea01.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    primaryKeys = ["photoId"],
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["userId"],
            childColumns = ["owner"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)

data class Photo(
    @PrimaryKey(autoGenerate = true) var photoId: Long?,
    val photoURL: String = "",
    val owner: Long?
)
