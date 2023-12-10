package es.unex.giiis.asee.snapmap_ea01.view.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import es.unex.giiis.asee.snapmap_ea01.SnapMapApplication
import es.unex.giiis.asee.snapmap_ea01.data.Repository
import es.unex.giiis.asee.snapmap_ea01.data.model.Photo
import es.unex.giiis.asee.snapmap_ea01.data.model.User
import kotlinx.coroutines.launch

class ImageViewModel(
    private val repository: Repository
) : ViewModel() {
    var user: User? = null
        set(value) {
            field = value
            repository.setUserid(value?.userId ?: 0)
        }

    private val _isLiked = MutableLiveData<Boolean>()
    val isLiked: LiveData<Boolean>
        get() = _isLiked

    private val _photoState: MutableLiveData<Photo?> = MutableLiveData()
    val photoState: LiveData<Photo?> = _photoState

    private val _ownerUsername = MutableLiveData<String>()
    val ownerUsername: LiveData<String> = _ownerUsername

    fun getPhoto(photoId: Long) {
        viewModelScope.launch {
            val photo = repository.getPhotoById(photoId)
            _photoState.value = photo

        }
    }

    fun getOwnerPhoto(photoId: Long) {
        viewModelScope.launch {
            val photo = repository.getPhotoById(photoId)
            _photoState.value = photo

            photo.owner?.let { ownerId ->
                repository.getUserById(ownerId).username.let { username ->
                    _ownerUsername.value = username
                }
            }
        }
    }

    fun isLiked(photoId: Long, user: User?) {
        viewModelScope.launch {
            val userId = user?.userId ?: 0
            val likeExists = repository.isPhotoLiked(userId, photoId)
            _isLiked.value = likeExists
        }
    }


    fun changeLikeStatus(photoId: Long, user: User?) {
        viewModelScope.launch {
            val userId = user?.userId ?: 0
            val isCurrentlyLiked = repository.isPhotoLiked(userId, photoId)

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
                // Get the Application object from extras
                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])

                return ImageViewModel(
                    (application as SnapMapApplication).appContainer.repository
                ) as T
            }
        }
    }
}