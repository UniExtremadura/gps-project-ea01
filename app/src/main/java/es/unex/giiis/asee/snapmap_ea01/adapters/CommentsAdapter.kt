package es.unex.giiis.asee.snapmap_ea01.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import es.unex.giiis.asee.snapmap_ea01.R
import es.unex.giiis.asee.snapmap_ea01.data.model.Comment
import es.unex.giiis.asee.snapmap_ea01.database.UserDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class CommentsAdapter(private var comments: List<Comment>, private val userDao: UserDao,
                      private val coroutineScope: CoroutineScope) :
    RecyclerView.Adapter<CommentsAdapter.CommentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_comment, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = comments[position]
        holder.bind(comment, userDao)
    }

    override fun getItemCount(): Int {
        return comments.size
    }

    // Actualiza la lista de comentarios en el adaptador
    fun updateComments(newComments: List<Comment>) {
        comments = newComments
        notifyDataSetChanged()
    }

    inner class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvAuthor: TextView = itemView.findViewById(R.id.tvCommentAuthor)
        private val tvComment: TextView = itemView.findViewById(R.id.tvCommentText)

        fun bind(comment: Comment, userDao: UserDao) {
            coroutineScope.launch {
                // Accede a la base de datos para obtener el usuario correspondiente
                val user = userDao.getUserById(comment.author ?: 0)

                tvAuthor.text = user.username
                tvComment.text = comment.comment
            }
        }
    }
}