package es.unex.giiis.asee.snapmap_ea01.view.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import es.unex.giiis.asee.snapmap_ea01.SnapMapApplication
import es.unex.giiis.asee.snapmap_ea01.api.APIError
import es.unex.giiis.asee.snapmap_ea01.data.Repository
import es.unex.giiis.asee.snapmap_ea01.data.model.Photo
import es.unex.giiis.asee.snapmap_ea01.data.model.User
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class CameraViewModel(
    private val repository: Repository
) : ViewModel(){

    var user: User? = null
        set(value) {
            field = value
            repository.setUserid(value!!.userId!!)
        }

    val photosURI = repository.photos

    private val toastMessage = MutableLiveData<String?>()

    init {
        refresh()
    }

    fun uploadPhoto(photo:String, lat: Double, lon: Double){
        val uploadedPhoto = Photo(
            photoId = null,
            photoURL = photo,
            owner = user?.userId,
            lat = lat,
            lon = lon
        )

        viewModelScope.launch {
            val photoId = repository.uploadPhoto(uploadedPhoto)
            Log.d("API", "Photo uploaded with id: $photoId")
        }
        showToast("Photo uploaded successfully")
    }

    private fun refresh() {
        launchDataLoad { repository.tryUpdateRecentPhotosCache() }
    }

    private fun showToast(message: String) {
        toastMessage.value = message
    }

    private fun clearToastMessage() {
        toastMessage.value = null
    }
    private fun launchDataLoad(block: suspend () -> Unit): Job {
        return viewModelScope.launch {
            try {
                // Add a reload Cache message
                showToast("Checking for updates from cache")
                block()
            } catch (error: APIError) {
                showToast(error.message!!)
            } finally {
                showToast("Checked successfully")
                clearToastMessage()
            }
        }
    }

    companion object {

        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                // Get the Application object from extras
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])

                return CameraViewModel(
                    (application as SnapMapApplication).appContainer.repository) as T
            }
        }
    }
}