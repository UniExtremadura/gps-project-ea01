package es.unex.giiis.asee.snapmap_ea01.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import es.unex.giiis.asee.snapmap_ea01.api.APIError
import es.unex.giiis.asee.snapmap_ea01.api.DogAPI
import es.unex.giiis.asee.snapmap_ea01.data.model.Photo
import es.unex.giiis.asee.snapmap_ea01.data.model.User
import es.unex.giiis.asee.snapmap_ea01.database.CommentDao
import es.unex.giiis.asee.snapmap_ea01.database.PhotoDao
import es.unex.giiis.asee.snapmap_ea01.database.PhotoURIDao
import es.unex.giiis.asee.snapmap_ea01.database.UserDao
import es.unex.giiis.asee.snapmap_ea01.database.UserPhotoLikeRefDao
import es.unex.giiis.asee.snapmap_ea01.database.UserUserFollowRefDao

class Repository(
    private val userDao: UserDao,
    private val userUserFollowRefDao: UserUserFollowRefDao,
    private val userPhotoLikeRefDao: UserPhotoLikeRefDao,
    private val commentDao: CommentDao,
    private val photoDao: PhotoDao,
    private val photoURIDao: PhotoURIDao,
    private val networkService: DogAPI
) {
    private var lastUpdateTimeMillis: Long = 0L

    val photos = photoURIDao.getPhotos()

    private val userFilter = MutableLiveData<Long>()

    val photosFromFollowedUsers: LiveData<List<Photo>> =
        userFilter.switchMap{ userid -> photoDao.getPhotosFromFollowedUsers(userid) }

    /**
     * Update the shows cache.
     *
     * This function may decide to avoid making a network requests on every call based on a
     * cache-invalidation policy.
     */
    suspend fun tryUpdateRecentPhotosCache() {
        if (shouldUpdatePhotosCache())
            fetchRecentPhotos()
    }

    suspend fun uploadPhoto(photo: Photo): Long {
        return photoDao.insertPhoto(photo)
    }

    /**
     * Fetch a new list of photos from the network, and append them to [PhotoDao]
     */
    private suspend fun fetchRecentPhotos() {
        try {

            val photos = networkService.getImages().map { it.toPhoto() }
            photoURIDao.deletePhotos()
            photoURIDao.insertPhotos(photos)
            lastUpdateTimeMillis = System.currentTimeMillis()

        } catch (cause: Throwable) {
            throw APIError("Unable to fetch data from API", cause)
            cause.printStackTrace()
        }
    }

    /**
     * Returns true if we should make a network request.
     */
    private suspend fun shouldUpdatePhotosCache(): Boolean {
        val lastFetchTimeMillis = lastUpdateTimeMillis
        val timeFromLastFetch = System.currentTimeMillis() - lastFetchTimeMillis
        return timeFromLastFetch > MIN_TIME_FROM_LAST_FETCH_MILLIS || photoURIDao.getNumberOfPhotos() == 0L
    }

    fun setUserid(userid: Long) {
        userFilter.value = userid
    }

    suspend fun insertUser(user: User): Long {
        return userDao.insertUser(user)
    }

    suspend fun getUserByUsername(username: String): User {
        return userDao.getUserByUsername(username)
    }

    suspend fun getOwnerOfPhoto(photoId: Long): String {
        return userDao.getUserById(photoId).username
    }

    companion object {
        private const val MIN_TIME_FROM_LAST_FETCH_MILLIS: Long = 3000

        @Volatile
        private var INSTANCE: Repository? = null

        fun getInstance(
            userDao: UserDao,
            userUserFollowRefDao: UserUserFollowRefDao,
            userPhotoLikeRefDao: UserPhotoLikeRefDao,
            commentDao: CommentDao,
            photoDao: PhotoDao,
            photoURIDao: PhotoURIDao,
            photoAPI: DogAPI
        ): Repository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Repository(userDao,userUserFollowRefDao, userPhotoLikeRefDao, commentDao, photoDao, photoURIDao, photoAPI).also { INSTANCE = it }
            }
        }
    }
}