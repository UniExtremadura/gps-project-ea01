package es.unex.giiis.asee.snapmap_ea01.data

import es.unex.giiis.asee.snapmap_ea01.api.APIError
import es.unex.giiis.asee.snapmap_ea01.api.DogAPI
import es.unex.giiis.asee.snapmap_ea01.database.PhotoDao
import es.unex.giiis.asee.snapmap_ea01.database.PhotoURIDao

class Repository private constructor(
    private val photoDao: PhotoDao,
    private val photoURIDao: PhotoURIDao,
    private val networkService: DogAPI
) {
    private var lastUpdateTimeMillis: Long = 0L

    val photos = photoURIDao.getPhotos()

    /**
     * Update the shows cache.
     *
     * This function may decide to avoid making a network requests on every call based on a
     * cache-invalidation policy.
     */
    suspend fun tryUpdateRecentPhotosCache() {
        if (shouldUpdatePhotosCache()) fetchRecentPhotos()
    }

    /**
     * Fetch a new list of photos from the network, and append them to [PhotoDao]
     */
    private suspend fun fetchRecentPhotos() {
        try {

            val photos = networkService.getImages().map { it.toPhoto() }
            /*
                Before inserting the photos, it is necessary to delete the previous ones, as their
                IDs will be different. This is because all the photos come from an API that provides
                random photos.
             */
            photoURIDao.deletePhotos()
            photoURIDao.insertPhotos(photos)
            lastUpdateTimeMillis = System.currentTimeMillis()

        } catch (cause: Throwable) {
            throw APIError("Unable to fetch data from API", cause)
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

    companion object {
        private const val MIN_TIME_FROM_LAST_FETCH_MILLIS: Long = 30000

        @Volatile
        private var INSTANCE: Repository? = null

        fun getInstance(
            photoDao: PhotoDao,
            photoURIDao: PhotoURIDao,
            photoAPI: DogAPI
        ): Repository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Repository(photoDao, photoURIDao, photoAPI).also { INSTANCE = it }
            }
        }
    }
}