package es.unex.giiis.asee.snapmap_ea01.view.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import es.unex.giiis.asee.snapmap_ea01.SnapMapApplication
import es.unex.giiis.asee.snapmap_ea01.data.Repository
import es.unex.giiis.asee.snapmap_ea01.data.model.User
import es.unex.giiis.asee.snapmap_ea01.data.model.UserUserFollowRef
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchViewModel (
    private val repository: Repository
) : ViewModel(){

    val following = repository.following
    private var users = mutableListOf<User>()
    var user: User? = null
        set(value) {
            field = value
            repository.setUserid(value!!.userId!!)
        }

    fun getUsers(): List<User> {
        viewModelScope.launch {
            users = repository.getUsers().toMutableList()
        }
        return users
    }

    fun followUser(userFollowRef: UserUserFollowRef) {
        viewModelScope.launch {
            repository.insertUserUserFollowRef(userFollowRef)
        }
    }

    fun unfollowUser(userFollowRef: UserUserFollowRef) {
        viewModelScope.launch {
            repository.deleteUserFollowRef(userFollowRef)
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

                return SearchViewModel(
                    (application as SnapMapApplication).appContainer.repository,

                    ) as T
            }
        }
    }
}