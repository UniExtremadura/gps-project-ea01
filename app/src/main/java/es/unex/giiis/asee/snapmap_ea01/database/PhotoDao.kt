package es.unex.giiis.asee.snapmap_ea01.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import es.unex.giiis.asee.snapmap_ea01.data.model.Photo

@Dao
interface PhotoDao {
    @Query("SELECT * FROM photo WHERE photoId = :photoId LIMIT 1")
    suspend fun getPhoto(photoId: Long): Photo

    @Insert
    suspend fun insertPhoto(photo: Photo): Long
}
