package es.unex.giiis.asee.snapmap_ea01.database

import androidx.lifecycle.LiveData
import es.unex.giiis.asee.snapmap_ea01.data.model.UserUserFollowRef
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import es.unex.giiis.asee.snapmap_ea01.data.model.User

@Dao
interface UserUserFollowRefDao {

    @Transaction
    @Query("SELECT * FROM user WHERE userId IN (SELECT user1 FROM useruserfollowref WHERE user2 = :user)")
    fun getUserFollowers(user: Long): LiveData<List<User>>

    @Transaction
    @Query("SELECT * FROM user WHERE userId IN (SELECT user2 FROM useruserfollowref WHERE user1 = :user)")
    fun getUserFollowing(user: Long): LiveData<List<User>>

    @Transaction
    @Insert
    suspend fun insertUserUserFollowRef(userUserFollowRef: UserUserFollowRef): Long


    @Transaction
    @Delete
    suspend fun deleteUserFollowRef(userUserFollowRef: UserUserFollowRef)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(userFollows: List<UserUserFollowRef>)
}
