package es.unex.giiis.asee.snapmap_ea01.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["userId"],
            childColumns = ["author"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Photo::class,
            parentColumns = ["photoId"],
            childColumns = ["photo"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [androidx.room.Index(value = ["author"]), androidx.room.Index(value = ["photo"])]

)

data class Comment(
    @PrimaryKey(autoGenerate = true) var commentId: Long?,
    var author: Long?,
    var photo: Long?,
    var comment: String?
)

