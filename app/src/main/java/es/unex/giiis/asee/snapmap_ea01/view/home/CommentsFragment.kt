package es.unex.giiis.asee.snapmap_ea01.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import es.unex.giiis.asee.snapmap_ea01.R
import es.unex.giiis.asee.snapmap_ea01.adapters.CommentsAdapter
import es.unex.giiis.asee.snapmap_ea01.database.SnapMapDatabase
import es.unex.giiis.asee.snapmap_ea01.data.model.Comment
import es.unex.giiis.asee.snapmap_ea01.data.model.User
import kotlinx.coroutines.launch

const val ARG_PHOTO_ID = "photoId"

class CommentsFragment : Fragment() {

    private var photoId: Long? = null
    private lateinit var db: SnapMapDatabase
    private lateinit var currentUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            photoId = it.getLong(ARG_PHOTO_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_comments, container, false)

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewComments)
        val editTextComment: EditText = view.findViewById(R.id.editTextComment)
        val btnAddComment: Button = view.findViewById(R.id.btnAddComment)

        // Configura el LayoutManager del RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Obtiene la instancia de SnapMapDatabase
        db = SnapMapDatabase.getInstance(requireContext())!!

        // Obtiene el objeto User de la actividad anterior
        currentUser =
            requireActivity().intent.getSerializableExtra(HomeActivity.USER_INFO) as User

        lifecycleScope.launch {
            try {
                // Obtén la lista de comentarios para la foto actual
                val comments = photoId?.let { db.commentDao().getCommentsForPhoto(it) }

                // Configura el adaptador para el RecyclerView
                val commentsAdapter =
                    CommentsAdapter(comments.orEmpty(), db.userDao(), lifecycleScope)
                recyclerView.adapter = commentsAdapter

                // Configura el botón para agregar comentarios
                btnAddComment.setOnClickListener {
                    val newCommentText = editTextComment.text.toString()
                    if (newCommentText.isNotEmpty()) {
                        val newComment = Comment(
                            author = currentUser.userId,
                            photo = photoId!!,
                            comment = newCommentText
                        )
                        // Inserta el nuevo comentario en la base de datos
                        lifecycleScope.launch {
                            db.commentDao().insertComment(newComment)
                            // Actualiza la lista de comentarios en el adaptador
                            commentsAdapter.updateComments(db.commentDao().getCommentsForPhoto(photoId!!))
                            editTextComment.text.clear()
                        }
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(photoId: Long): CommentsFragment {
            return CommentsFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_PHOTO_ID, photoId)
                }
            }
        }
    }
}
