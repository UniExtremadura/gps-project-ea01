package es.unex.giiis.asee.snapmap_ea01.data.model

import androidx.room.DatabaseView
import androidx.room.Entity
import androidx.room.ForeignKey
import java.io.Serializable

@Entity(primaryKeys = ["user1", "user2"],
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
    ]
)

data class UserUserFollowRef(
    val user1: Long,
    val user2: Long
)
