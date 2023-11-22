package es.unex.giiis.asee.snapmap_ea01.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import es.unex.giiis.asee.snapmap_ea01.data.model.UserPhotoLikeRef
import es.unex.giiis.asee.snapmap_ea01.data.model.UserUserFollowRef

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

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(userLikes: List<UserPhotoLikeRef>)

    @Query("SELECT COUNT(*) FROM userphotolikeref WHERE userId = :userId AND photoId = :photoId")
    suspend fun likeExists(userId: Long, photoId: Long): Int
}
