package es.unex.giiis.asee.snapmap_ea01.view.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import es.unex.giiis.asee.snapmap_ea01.SnapMapApplication
import es.unex.giiis.asee.snapmap_ea01.data.Repository
import es.unex.giiis.asee.snapmap_ea01.data.model.User
import kotlinx.coroutines.launch

class ProfileViewModel (
    private val repository: Repository
) : ViewModel(){

    val followers = repository.followers
    val following = repository.following
    var user: User? = null
        set(value) {
            field = value
            repository.setUserid(value!!.userId!!)
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

                return ProfileViewModel(
                    (application as SnapMapApplication).appContainer.repository,

                    ) as T
            }
        }
    }
}