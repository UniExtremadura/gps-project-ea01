package es.unex.giiis.asee.snapmap_ea01.view.home

import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import es.unex.giiis.asee.snapmap_ea01.SnapMapApplication
import es.unex.giiis.asee.snapmap_ea01.data.Repository
import es.unex.giiis.asee.snapmap_ea01.data.model.Photo
import es.unex.giiis.asee.snapmap_ea01.data.model.User
import kotlinx.coroutines.launch

class ImageViewModel(
    private val repository: Repository // Repositorio para acceder a los datos
) : ViewModel() {
    var user: User? = null
        set(value) {
            field = value
            // Actualiza el ID del usuario en el repositorio cuando se establece el usuario
            repository.setUserid(value?.userId ?: 0)
        }

    // LiveData para comprobar si la foto actual está marcada como "me gusta" por el usuario
    private val _isLiked = MutableLiveData<Boolean>()
    val isLiked: LiveData<Boolean>
        get() = _isLiked

    // LiveData para mantener el estado de la foto actual
    private val _photoState: MutableLiveData<Photo?> = MutableLiveData()
    val photoState: LiveData<Photo?> = _photoState

    // LiveData para almacenar el nombre de usuario del propietario de la foto
    private val _ownerUsername = MutableLiveData<String>()
    val ownerUsername: LiveData<String> = _ownerUsername

    // Obtiene la foto por su ID y actualiza el LiveData correspondiente
    fun getPhoto(photoId: Long) {
        viewModelScope.launch {
            val photo = repository.getPhotoById(photoId)
            _photoState.value = photo
        }
    }

    // Obtiene la foto y su propietario por el ID de la foto y actualiza los LiveData correspondientes
    fun getOwnerPhoto(photoId: Long) {
        viewModelScope.launch {
            val photo = repository.getPhotoById(photoId)
            _photoState.value = photo

            // Si la foto tiene un propietario, obtiene el nombre de usuario del propietario
            photo.owner?.let { ownerId ->
                repository.getUserById(ownerId).username.let { username ->
                    _ownerUsername.value = username
                }
            }
        }
    }

    // Verifica si la foto está marcada como "me gusta" por el usuario y actualiza el LiveData
    fun isLiked(photoId: Long, user: User?) {
        viewModelScope.launch {
            val userId = user?.userId ?: 0
            val likeExists = repository.isPhotoLiked(userId, photoId)
            _isLiked.value = likeExists
        }
    }

    // Cambia el estado de "me gusta" de la foto y actualiza el LiveData
    fun changeLikeStatus(photoId: Long, user: User?) {
        viewModelScope.launch {
            val userId = user?.userId ?: 0
            val isCurrentlyLiked = repository.isPhotoLiked(userId, photoId)

            // Si la foto ya está marcada como "me gusta", la desmarca y viceversa
            if (isCurrentlyLiked) {
                repository.removePhotoLike(userId, photoId)
                _isLiked.value = false
            } else {
                repository.addPhotoLike(userId, photoId)
                _isLiked.value = true
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
                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                return ImageViewModel(
                    (application as SnapMapApplication).appContainer.repository
                ) as T
            }
        }
    }
}