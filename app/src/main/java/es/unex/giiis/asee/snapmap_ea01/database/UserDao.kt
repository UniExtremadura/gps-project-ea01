package es.unex.giiis.asee.snapmap_ea01.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import es.unex.giiis.asee.snapmap_ea01.data.model.User

@Dao
interface UserDao {

    @Query("SELECT * FROM user WHERE userId = :id LIMIT 1")
    suspend fun getUserById(id: Long): User

    @Query("SELECT * FROM user WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): User

    @Query("SELECT username FROM user WHERE userId = :userId LIMIT 1")
    suspend fun getUserById(userId: Long?): String

    @Query ("SELECT * FROM user")
    suspend fun getUsers() : List<User>

    @Insert
    suspend fun insertUser(user: User): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(users: List<User>)

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)
}