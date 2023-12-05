package es.unex.giiis.asee.snapmap_ea01.data

import es.unex.giiis.asee.snapmap_ea01.api.APIError
import es.unex.giiis.asee.snapmap_ea01.data.api.PhotoURI
import es.unex.giiis.asee.snapmap_ea01.database.PhotoDao

class Repository private constructor(
    private val photoDao: PhotoDao,
    private val networkService: PhotoURI
) {
    private var lastUpdateTimeMillis: Long = 0L

    val photos = photoDao.getPhotos()

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

            TODO("Ajustar codigo a la nueva API")

            /*
            val photos = networkService.(1).tvShows.map { it.toShow()}
            photoDao.insertAll(photos)
            lastUpdateTimeMillis = System.currentTimeMillis()
            */

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
        return timeFromLastFetch > MIN_TIME_FROM_LAST_FETCH_MILLIS || photoDao.getNumberOfPhotos() == 0L
    }

    companion object {
        private const val MIN_TIME_FROM_LAST_FETCH_MILLIS: Long = 30000

        @Volatile
        private var INSTANCE: Repository? = null

        fun getInstance(
            photoDao: PhotoDao,
            photoAPI: PhotoURI
        ): Repository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Repository(photoDao, photoAPI).also { INSTANCE = it }
            }
        }
    }
}