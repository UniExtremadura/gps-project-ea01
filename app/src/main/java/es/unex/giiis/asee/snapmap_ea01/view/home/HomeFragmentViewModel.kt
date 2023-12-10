package es.unex.giiis.asee.snapmap_ea01.view.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import es.unex.giiis.asee.snapmap_ea01.SnapMapApplication
import es.unex.giiis.asee.snapmap_ea01.data.Repository
import es.unex.giiis.asee.snapmap_ea01.data.model.User

class HomeFragmentViewModel(
    private val repository: Repository
) : ViewModel() {
    var user: User? = null
        set(value) {
            field = value
            repository.setUserid(value?.userId ?: 0)
        }

    val photos = repository.photosFromFollowedUsers

    suspend fun getOwnerPhoto(owner: Long): String{
        return repository.getOwnerOfPhoto(owner)
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

                return HomeFragmentViewModel(
                    (application as SnapMapApplication).appContainer.repository,

                    ) as T
            }
        }
    }
}