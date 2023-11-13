package es.unex.giiis.asee.snapmap_ea01.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    primaryKeys = ["commentId"],
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
    ]
)

data class Comment(
    @PrimaryKey(autoGenerate = true) var commentId: Long?,
    val author: Long?,
    val photo: Long?
)

