package es.unex.giiis.asee.snapmap_ea01.data.model

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(primaryKeys = ["userId", "photoId"],
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Photo::class,
            parentColumns = ["photoId"],
            childColumns = ["photoId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [androidx.room.Index(value = ["userId"]), androidx.room.Index(value = ["photoId"])]
)

data class UserPhotoLikeRef(
    var userId: Long,
    var photoId: Long
)
