package es.unex.giiis.asee.snapmap_ea01.database

import android.content.Context
import es.unex.giiis.asee.snapmap_ea01.dummy.dummyUser
import es.unex.giiis.asee.snapmap_ea01.dummy.dummyPhoto
import es.unex.giiis.asee.snapmap_ea01.dummy.dummyUserPhotoLike
import es.unex.giiis.asee.snapmap_ea01.dummy.dummyUserUserFollow
import es.unex.giiis.asee.snapmap_ea01.dummy.dummyComment

class DatabaseInitializer {
    companion object {
        private var database: SnapMapDatabase? = null

        suspend fun initialize(context: Context) {
            database = SnapMapDatabase.getInstance(context)

            // Inserta los usuarios
            database?.userDao()?.insertAll(dummyUser)

            //Inserta las fotos
            database?.photoDao()?.insertAll(dummyPhoto)

            // Inserta los follows de cada usuario
            database?.userUserFollowRefDao()?.insertAll(dummyUserUserFollow)

            // Inserta los me gustas de un usuario a las fotos de otros usuarios
            database?.userPhotoLikeRefDao()?.insertAll(dummyUserPhotoLike)

            // Inserta los comentarios
            database?.commentDao()?.insertAll(dummyComment)

        }
    }
}