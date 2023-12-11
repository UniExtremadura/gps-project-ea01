package es.unex.giiis.asee.snapmap_ea01.view.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import es.unex.giiis.asee.snapmap_ea01.SnapMapApplication
import es.unex.giiis.asee.snapmap_ea01.adapters.CommentWithUser
import es.unex.giiis.asee.snapmap_ea01.data.Repository
import es.unex.giiis.asee.snapmap_ea01.data.model.Comment
import es.unex.giiis.asee.snapmap_ea01.data.model.User
import kotlinx.coroutines.launch

class CommentsViewModel(
    private val repository: Repository
) : ViewModel() {

    var user: User? = null
        set(value) {
            field = value
            repository.setUserid(value?.userId ?: 0)  // Actualizamos el userId en el repositorio
        }

    // LiveData que contiene la lista de comentarios con información del usuario
    private val _commentsWithUser = MutableLiveData<List<CommentWithUser>>()
    val commentsWithUser: LiveData<List<CommentWithUser>>
        get() = _commentsWithUser

    // Función para añadir un comentario a la foto
    fun addComment(photoId: Long, userId: Long, commentText: String) {
        viewModelScope.launch {
            val newComment = Comment(author = userId, photo = photoId, comment = commentText)
            repository.insertComment(newComment)  // Insertamos el nuevo comentario en la base de datos
            getCommentsForPhoto(photoId)  // Actualizamos la lista de comentarios
        }
    }

    // Función para obtener los comentarios de una foto
    fun getCommentsForPhoto(photoId: Long) {
        viewModelScope.launch {
            val commentsList = repository.getCommentsForPhoto(photoId)  // Obtenemos los comentarios de la foto de la base de datos
            val commentsWithUserList = commentsList.mapNotNull { comment ->
                comment.author?.let { authorId ->
                    CommentWithUser(comment, repository.getUserById(authorId))  // Creamos un objeto CommentWithUser para cada comentario
                }
            }
            _commentsWithUser.value = commentsWithUserList  // Actualizamos el LiveData con la nueva lista de comentarios
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
                return CommentsViewModel(
                    (application as SnapMapApplication).appContainer.repository
                ) as T
            }
        }
    }
}
