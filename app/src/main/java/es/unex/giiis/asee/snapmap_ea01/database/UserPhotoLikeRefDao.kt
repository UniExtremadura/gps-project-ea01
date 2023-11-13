package es.unex.giiis.asee.snapmap_ea01.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import es.unex.giiis.asee.snapmap_ea01.data.model.UserPhotoLikeRef

@Dao
interface UserPhotoLikeRefDao {
    @Query("SELECT * FROM userphotolikeref WHERE userId = :userId AND photoId = :photoId LIMIT 1")
    suspend fun getUserPhotoLikeRef(userId: Long, photoId: Long): UserPhotoLikeRef?

    @Insert
    suspend fun insertUserPhotoLikeRef(userPhotoLikeRef: UserPhotoLikeRef): Long

    @Update
    suspend fun updatePhotoLikeRef(userPhotoLikeRef: UserPhotoLikeRef)

    @Delete
    suspend fun deletePhotoLikeRef(userPhotoLikeRef: UserPhotoLikeRef)
}
