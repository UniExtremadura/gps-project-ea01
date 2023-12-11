package es.unex.giiis.asee.snapmap_ea01.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import es.unex.giiis.asee.snapmap_ea01.R
import es.unex.giiis.asee.snapmap_ea01.data.model.Comment
import es.unex.giiis.asee.snapmap_ea01.data.model.User

// Clase de datos que agrupa un Comment y un User en un solo objeto
data class CommentWithUser(
    val comment: Comment,
    val user: User
)

// Adaptador para el RecyclerView que muestra los comentarios
class CommentsAdapter :
    ListAdapter<CommentWithUser, CommentsAdapter.CommentViewHolder>(CommentDiffCallback()) {

    // Crea una nueva vista para elemento de la lista
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_comment, parent, false)
        return CommentViewHolder(view)
    }

    // Vincula los datos de un elemento de la lista a la vista
    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val commentWithUser = getItem(position)
        holder.bind(commentWithUser)
    }

    // ViewHolder que representa un elemento de la lista
    class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvAuthor: TextView = itemView.findViewById(R.id.tvCommentAuthor)
        private val tvComment: TextView = itemView.findViewById(R.id.tvCommentText)

        // Vincula los datos de CommentWithUser a la vista
        fun bind(commentWithUser: CommentWithUser) {
            tvAuthor.text = commentWithUser.user.username
            tvComment.text = commentWithUser.comment.comment
        }
    }

    // Calcula la diferencia entre dos elementos de la lista
    // Permite animaciones automáticas en el RecyclerView cuando se actualiza el contenido
    class CommentDiffCallback : DiffUtil.ItemCallback<CommentWithUser>() {
        // Comprueba si dos elementos representan el mismo objeto
        override fun areItemsTheSame(oldItem: CommentWithUser, newItem: CommentWithUser): Boolean {
            return oldItem.comment.commentId == newItem.comment.commentId
        }

        // Comprueba si los contenidos de dos elementos son iguales
        override fun areContentsTheSame(oldItem: CommentWithUser, newItem: CommentWithUser): Boolean {
            return oldItem == newItem
        }
    }
}