package es.unex.giiis.asee.snapmap_ea01.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import es.unex.giiis.asee.snapmap_ea01.data.model.Photo
import es.unex.giiis.asee.snapmap_ea01.data.model.User
import es.unex.giiis.asee.snapmap_ea01.data.model.UserPhotoLikeRef
import es.unex.giiis.asee.snapmap_ea01.data.model.UserUserFollowRef
import es.unex.giiis.asee.snapmap_ea01.data.model.Comment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Database(entities = [User::class, Photo::class, UserPhotoLikeRef::class, UserUserFollowRef::class, Comment::class], version = 5)
abstract class SnapMapDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    abstract fun photoDao(): PhotoDao

    abstract fun userPhotoLikeRefDao(): UserPhotoLikeRefDao

    abstract fun userUserFollowRefDao(): UserUserFollowRefDao

    abstract fun commentDao(): CommentDao

    companion object {
        private var INSTANCE: SnapMapDatabase? = null
        fun getInstance(context: Context): SnapMapDatabase? {
            if (INSTANCE == null) {
                synchronized(SnapMapDatabase::class) {
                    INSTANCE = databaseBuilder(
                        context.applicationContext,
                        SnapMapDatabase::class.java, "snapmap.db"
                    )
                        .fallbackToDestructiveMigration()
                        .build();

                    CoroutineScope(Dispatchers.IO).launch {
                        // Inicializa la base de datos cuando se crea la instancia
                        DatabaseInitializer.initialize(context.applicationContext)
                    }
                }
            }
            return INSTANCE!!
        }
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}