package es.unex.giiis.asee.snapmap_ea01.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import es.unex.giiis.asee.snapmap_ea01.data.model.User

@Dao
interface UserDao {
    @Query("SELECT * FROM user WHERE username = :username LIMIT 1")
    suspend fun getUser(username: String): User

    @Insert
    suspend fun insertUser(user: User): Long

    @Update
    suspend fun updateUser(user: User)
}