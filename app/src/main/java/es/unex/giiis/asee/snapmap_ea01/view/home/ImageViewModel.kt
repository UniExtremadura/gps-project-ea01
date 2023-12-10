package es.unex.giiis.asee.snapmap_ea01.view.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import es.unex.giiis.asee.snapmap_ea01.SnapMapApplication
import es.unex.giiis.asee.snapmap_ea01.data.Repository
import es.unex.giiis.asee.snapmap_ea01.data.model.Photo
import es.unex.giiis.asee.snapmap_ea01.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ImageViewModel(
    private val repository: Repository
) : ViewModel() {

    var user: User? = null
        set(value) {
            field = value
            repository.setUserid(value!!.userId!!)
        }

    private val _isLiked = MutableLiveData<Boolean>()
    val isLiked: LiveData<Boolean>
        get() = _isLiked

    private val _photoState: MutableLiveData<Photo?> = MutableLiveData()
    val photoState: LiveData<Photo?> = _photoState

    var ownerUsername: String = ""

    private var currentUser: User? = null
    private var currentPhotoId: Long = -1

    fun getPhoto(photoId: Long) {
        viewModelScope.launch {
            val photo = repository.getPhotoById(photoId)
            _photoState.value = photo

            // Guardamos el usuario actual y el id de la foto
            currentUser = repository.getUserById(photo.owner ?: 0)
          //  currentPhotoId = photo.photoId ?: -1
        }
    }

    fun getOwnerPhoto(photoId: Long) {
        viewModelScope.launch {
            val photo = repository.getPhotoById(photoId)
            _photoState.value = photo

            // Guardamos el id del propietario
            val ownerId = photo.owner ?: 0

            // Obtenemos el nombre del usuario correspondiente al ownerId
            ownerUsername = repository.getUserById(ownerId).username
        }
    }

    fun isLiked() {
        viewModelScope.launch {
            val likeExists = repository.isPhotoLiked(
                userId = currentUser?.userId ?: 0,
                photoId = currentPhotoId
            )
            _isLiked.value = likeExists
        }
    }

    fun changeLikeStatus(photoId: Long, user: User?) {
        viewModelScope.launch {
            _isLiked.value = _isLiked.value?.not() ?: false // Invertir el valor solo si no es nulo

            if (_isLiked.value == true) {
                // Código para agregar el "like"
                repository.addPhotoLike(
                    userId = user?.userId ?: 0,
                    photoId = photoId
                )
            } else {
                // Código para quitar el "like"
                repository.removePhotoLike(
                    userId = user?.userId ?: 0,
                    photoId = photoId
                )
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

                return ImageViewModel(
                    (application as SnapMapApplication).appContainer.repository) as T
            }
        }
    }
}