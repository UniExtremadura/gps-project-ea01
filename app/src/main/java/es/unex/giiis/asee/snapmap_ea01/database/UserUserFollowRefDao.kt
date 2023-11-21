package es.unex.giiis.asee.snapmap_ea01.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import es.unex.giiis.asee.snapmap_ea01.data.model.UserUserFollowRef

@Dao
interface UserUserFollowRefDao {
    @Query("SELECT * FROM useruserfollowref WHERE user1 = :user1 AND user2 = :user2 LIMIT 1")
    suspend fun getUserUserFollowRef(user1: Long, user2: Long): UserUserFollowRef?

    @Query("SELECT * FROM useruserfollowref WHERE user2 = :user")
    suspend fun getFollowers(user:Long): List<UserUserFollowRef>

    @Query("SELECT * FROM useruserfollowref WHERE user1 = :user")
    suspend fun getFollowing(user:Long): List<UserUserFollowRef>

    @Insert
    suspend fun insertUserUserFollowRef(userUserFollowRef: UserUserFollowRef): Long

    @Update
    suspend fun updateUserFollowRef(userUserFollowRef: UserUserFollowRef)

    @Delete
    suspend fun deleteUserFollowRef(userUserFollowRef: UserUserFollowRef)
}
