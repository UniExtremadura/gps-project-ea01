package es.unex.giiis.asee.snapmap_ea01.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import es.unex.giiis.asee.snapmap_ea01.data.model.Photo
import es.unex.giiis.asee.snapmap_ea01.data.model.User

@Dao
interface PhotoDao {
    @Query("SELECT * FROM photo WHERE photoId = :photoId LIMIT 1")
    suspend fun getPhoto(photoId: Long): Photo

    @Query("SELECT * FROM photo")
    suspend fun getAllPhotos(): List<Photo>

    @Insert
    suspend fun insertPhoto(photo: Photo): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(photos: List<Photo>)
}
