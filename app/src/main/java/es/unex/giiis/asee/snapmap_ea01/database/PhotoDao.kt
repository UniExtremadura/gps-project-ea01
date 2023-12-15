package es.unex.giiis.asee.snapmap_ea01.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import es.unex.giiis.asee.snapmap_ea01.data.model.Photo

@Dao
interface PhotoDao {
    @Query("SELECT * FROM photo WHERE photoId = :photoId LIMIT 1")
    suspend fun getPhoto(photoId: Long): Photo

    @Query("SELECT * FROM photo WHERE owner = :userId OR owner IN (SELECT user2 FROM useruserfollowref WHERE user1 = :userId)")
    fun getPhotosFromFollowedUsers(userId: Long): LiveData<List<Photo>>

    @Transaction
    @Insert
    suspend fun insertPhoto(photo: Photo): Long

    @Query("SELECT * FROM photo")
    fun getPhotos(): LiveData<List<Photo>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(photos: List<Photo>)

}
