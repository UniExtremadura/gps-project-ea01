package es.unex.giiis.asee.snapmap_ea01.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import es.unex.giiis.asee.snapmap_ea01.data.model.Photo
import es.unex.giiis.asee.snapmap_ea01.data.model.User
import es.unex.giiis.asee.snapmap_ea01.data.model.UserPhotoLikeRef
import es.unex.giiis.asee.snapmap_ea01.data.model.UserUserFollowRef


@Database(entities = [User::class, Photo::class, UserPhotoLikeRef::class, UserUserFollowRef::class], version = 3)
abstract class SnapMapDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    abstract fun photoDao(): PhotoDao

    abstract fun userPhotoLikeRefDao(): UserPhotoLikeRefDao

    abstract fun userUserFollowRefDao(): UserUserFollowRefDao

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

                }
            }
            return INSTANCE
        }
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}