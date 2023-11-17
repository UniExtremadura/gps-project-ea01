package es.unex.giiis.asee.snapmap_ea01.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    primaryKeys = ["user1", "user2"],
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["userId"],
            childColumns = ["user1"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = ["userId"],
            childColumns = ["user2"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["user1"]),
        Index(value = ["user2"])
    ]
)
data class UserUserFollowRef(
    var user1: Long,
    var user2: Long
)