package es.unex.giiis.asee.snapmap_ea01.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import es.unex.giiis.asee.snapmap_ea01.data.model.Comment

@Dao
interface CommentDao {
    @Query("SELECT * FROM comment WHERE commentId = :commentId LIMIT 1")
    suspend fun getComment(commentId: Long): Comment

    @Insert
    suspend fun insertComment(comment: Comment): Long

    @Update
    suspend fun updateComment(comment: Comment)

    @Delete
    suspend fun deleteComment(comment: Comment)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(comments: List<Comment>)

    @Query("SELECT * FROM comment")
    suspend fun getAllComments(): List<Comment>

    @Query("SELECT * FROM comment WHERE photo = :photoId")
    suspend fun getCommentsForPhoto(photoId: Long): List<Comment>

}
