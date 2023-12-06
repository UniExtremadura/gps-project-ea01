package es.unex.giiis.asee.snapmap_ea01.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import es.unex.giiis.asee.snapmap_ea01.data.model.PhotoURI_DB

@Dao
interface PhotoURIDao {
    @Query("SELECT * FROM photouri_db")
    fun getPhotos(): LiveData<List<PhotoURI_DB>>

    @Query("SELECT count(*) FROM photouri_db")
    suspend fun getNumberOfPhotos(): Long

    @Insert
    suspend fun insertPhotos(photos: List<PhotoURI_DB>)

    @Query("DELETE FROM photouri_db")
    suspend fun deletePhotos()
}