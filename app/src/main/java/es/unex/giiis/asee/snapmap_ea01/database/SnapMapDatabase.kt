package es.unex.giiis.asee.snapmap_ea01.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import es.unex.giiis.asee.snapmap_ea01.data.model.User

@Database(entities = [User::class], version = 1)
abstract class SnapMapDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    companion object {
        private var INSTANCE: SnapMapDatabase? = null
        fun getInstance(context: Context): SnapMapDatabase? {
            if (INSTANCE == null) {
                synchronized(SnapMapDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context,
                        SnapMapDatabase::class.java,
                        "snapmap.db"
                    ).build()
                }
            }
            return INSTANCE
        }
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}