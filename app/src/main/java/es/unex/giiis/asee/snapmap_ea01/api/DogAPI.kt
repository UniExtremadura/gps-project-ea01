package es.unex.giiis.asee.snapmap_ea01.api

import es.unex.giiis.asee.snapmap_ea01.data.api.PhotoURI
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

private val service: DogAPI by lazy {
    val okHttpClient = OkHttpClient.Builder()
        //.addInterceptor(SkipNetworkInterceptor())
        .addInterceptor(HttpLoggingInterceptor())
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl("https://dog.ceo/api/breeds/image/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    retrofit.create(DogAPI::class.java)
}

fun getNetworkService() = service

interface DogAPI {
    @GET("random")
    suspend fun getDog(): PhotoURI
}

class APIError(message: String, cause: Throwable?) : Throwable(message, cause)

interface APICallback {
    fun onCompleted(photo: String)
    fun onError(cause: Throwable)
}