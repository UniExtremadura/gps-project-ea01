package es.unex.giiis.asee.snapmap_ea01.view.home

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
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
            repository.setUserid(value?.userId ?: 0)
        }

    private val _commentsWithUser = MutableLiveData<List<CommentWithUser>>()
    val commentsWithUser: LiveData<List<CommentWithUser>>
        get() = _commentsWithUser

    fun addComment(photoId: Long, userId: Long, commentText: String) {
        viewModelScope.launch {
            try {
                Log.d("CommentsViewModel", "Intentando agregar comentario")
                val newComment = Comment(author = userId, photo = photoId, comment = commentText)
                repository.insertComment(newComment)
                Log.d("CommentsViewModel", "Comentario agregado, actualizando lista de comentarios")
                getCommentsForPhoto(photoId) // Actualizamos la lista de comentarios
            } catch (e: SQLiteConstraintException) {
                Log.e("CommentsViewModel", "Error de restricción de clave foránea: ${e.message}")
                // Aquí puedes agregar lógica adicional para manejar este error específico
            } catch (e: Exception) {
                Log.e("CommentsViewModel", "Error al agregar comentario: ${e.message}")
            }
        }
    }

    fun getCommentsForPhoto(photoId: Long) {
        viewModelScope.launch {
            try {
                Log.d("CommentsViewModel", "Obteniendo comentarios para la foto con ID: $photoId")
                val commentsList = repository.getCommentsForPhoto(photoId)
                val commentsWithUserList = commentsList.mapNotNull { comment ->
                    comment.author?.let { authorId ->
                        CommentWithUser(comment, repository.getUserById(authorId))
                    }
                }
                Log.d("CommentsViewModel", "Comentarios obtenidos: ${commentsWithUserList.size}")
                _commentsWithUser.value = commentsWithUserList
            } catch (e: Exception) {
                Log.e("CommentsViewModel", "Error al obtener comentarios: ${e.message}")
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

                return CommentsViewModel(
                    (application as SnapMapApplication).appContainer.repository
                ) as T
            }
        }
    }
}