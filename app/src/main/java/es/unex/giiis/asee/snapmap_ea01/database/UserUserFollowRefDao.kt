package es.unex.giiis.asee.snapmap_ea01.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import es.unex.giiis.asee.snapmap_ea01.data.model.User
import es.unex.giiis.asee.snapmap_ea01.data.model.UserUserFollowRef

@Dao
interface UserUserFollowRefDao {

    @Transaction
    @Query("SELECT * FROM user WHERE userId IN (SELECT user1 FROM useruserfollowref WHERE user2 = :user)")
    fun getUserFollowers(user: Long): LiveData<List<User>>
    @Query("SELECT * FROM useruserfollowref WHERE user2 = :user")
    fun getFollowers(user:Long): LiveData<List<UserUserFollowRef>>

    @Transaction
    @Query("SELECT * FROM user WHERE userId IN (SELECT user2 FROM useruserfollowref WHERE user1 = :user)")
    fun getUserFollowing(user: Long): LiveData<List<User>>
    @Query("SELECT * FROM useruserfollowref WHERE user1 = :user")
    fun getFollowing(user:Long): LiveData<List<UserUserFollowRef>>

    @Transaction
    @Insert
    suspend fun insertUserUserFollowRef(userUserFollowRef: UserUserFollowRef): Long


    @Transaction
    @Delete
    suspend fun deleteUserFollowRef(userUserFollowRef: UserUserFollowRef)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(userFollows: List<UserUserFollowRef>)
}
