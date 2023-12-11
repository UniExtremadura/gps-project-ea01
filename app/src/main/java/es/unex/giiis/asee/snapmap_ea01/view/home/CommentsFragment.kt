package es.unex.giiis.asee.snapmap_ea01.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import es.unex.giiis.asee.snapmap_ea01.adapters.CommentsAdapter
import es.unex.giiis.asee.snapmap_ea01.databinding.FragmentCommentsBinding

const val ARG_PHOTO_ID = "photoId"

class CommentsFragment : Fragment() {

    // Referencias a la vista, el adaptador y los ViewModels
    private lateinit var binding: FragmentCommentsBinding
    private lateinit var commentsAdapter: CommentsAdapter
    private val viewModel: CommentsViewModel by viewModels { CommentsViewModel.Factory }
    private val homeViewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCommentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtenemos el ID de la foto del argumento
        val photoId = arguments?.getLong(ARG_PHOTO_ID) ?: -1

        // Observamos el usuario en homeViewModel y lo asignamos a viewModel.user
        homeViewModel.user.observe(viewLifecycleOwner) { user ->
            viewModel.user = user
        }

        // Configuramos el RecyclerView y el Adapter
        commentsAdapter = CommentsAdapter()
        binding.recyclerViewComments.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = commentsAdapter
        }

        // Observamos los comentarios asociados a una foto específica
        observeComments(photoId)

        // Establecemos el OnClickListener para el botón de añadir comentario
        binding.btnAddComment.setOnClickListener {
            addComment(photoId)
        }
    }

    private fun observeComments(photoId: Long) {
        viewModel.getCommentsForPhoto(photoId)
        viewModel.commentsWithUser.observe(viewLifecycleOwner) { commentsWithUser ->
            // Actualizamos el adaptador con los nuevos comentarios
            commentsAdapter.submitList(commentsWithUser)
        }
    }

    private fun addComment(photoId: Long) {
        val commentText = binding.editTextComment.text.toString().trim()

        // Si el comentario está vacío, mostramos un mensaje de error y salimos del método
        if (commentText.isEmpty()) {
            Toast.makeText(context, "El comentario no puede estar vacío", Toast.LENGTH_SHORT).show()
            return
        }

        // Si el usuario es válido, añadimos el comentario
        homeViewModel.user.value?.let { user ->
            val userId = user.userId
            if (userId != null) {
                // Añadimos el comentario utilizando el ViewModel
                viewModel.addComment(photoId, userId, commentText)
            }
            // Limpiamos el campo de texto del comentario
            binding.editTextComment.text.clear()
        } ?: run {
            // Si el usuario no es válido, mostramos un mensaje de error
            Toast.makeText(context, "El ID del usuario no es válido", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        fun newInstance(photoId: Long): CommentsFragment {
            return CommentsFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_PHOTO_ID, photoId)
                }
            }
        }
    }
}