package es.unex.giiis.asee.snapmap_ea01.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import es.unex.giiis.asee.snapmap_ea01.data.model.User

@Dao
interface UserDao {
    @Query("SELECT * FROM user WHERE username = :username LIMIT 1")
    suspend fun getUser(username: String): User

    @Query ("SELECT * FROM user")
    fun getUsers() : List<User>

    @Insert
    suspend fun insertUser(user: User): Long
}