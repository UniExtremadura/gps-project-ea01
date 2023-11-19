package es.unex.giiis.asee.snapmap_ea01.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import es.unex.giiis.asee.snapmap_ea01.R
import es.unex.giiis.asee.snapmap_ea01.adapters.CommentsAdapter
import es.unex.giiis.asee.snapmap_ea01.database.SnapMapDatabase
import kotlinx.coroutines.launch

private const val ARG_PHOTO_ID = "photoId"

class CommentsFragment : Fragment() {

    private var photoId: Long? = null

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

        // Configura el LayoutManager del RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Obtiene el commentDao de la base de datos
        val commentDao = SnapMapDatabase.getInstance(requireContext())?.commentDao()

        // Obtiene el userDao de la base de datos
        val userDao = SnapMapDatabase.getInstance(requireContext())?.userDao()

        commentDao?.let { dao ->
            userDao?.let { user ->
                lifecycleScope.launch {
                    try {
                        val comments = photoId?.let { dao.getCommentsForPhoto(it) }
                        val commentsAdapter = CommentsAdapter(comments.orEmpty(), user, lifecycleScope)
                        recyclerView.adapter = commentsAdapter
                    } catch (e: Exception) {
                        // Manejar la excepción (puedes mostrar un mensaje de error o registrar el error)
                        e.printStackTrace()
                    }
                }
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