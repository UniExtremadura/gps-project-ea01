package es.unex.giiis.asee.snapmap_ea01.view.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import es.unex.giiis.asee.snapmap_ea01.adapters.CommentsAdapter
import es.unex.giiis.asee.snapmap_ea01.databinding.FragmentCommentsBinding

const val ARG_PHOTO_ID = "photoId"

class CommentsFragment : Fragment() {

    private lateinit var binding: FragmentCommentsBinding
    private lateinit var commentsAdapter: CommentsAdapter

    private val viewModel: CommentsViewModel by viewModels { CommentsViewModel.Factory }

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
        Log.d("CommentsFragment", "photoId obtenido: $photoId")

        // Configuramos el RecyclerView y el Adapter
        commentsAdapter = CommentsAdapter()
        binding.recyclerViewComments.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = commentsAdapter
        }

        // Observamos los comentarios del ViewModel
        viewModel.getCommentsForPhoto(photoId)
        viewModel.commentsWithUser.observe(viewLifecycleOwner) { commentsWithUser ->
            Log.d("CommentsFragment", "Actualizando adaptador con ${commentsWithUser.size} comentarios")
            commentsAdapter.submitList(commentsWithUser)
        }

        // Establecemos el OnClickListener para el botón de añadir comentario
        binding.btnAddComment.setOnClickListener {
            val commentText = binding.editTextComment.text.toString()
            if (commentText.isNotEmpty()) {
                viewModel.addComment(photoId, viewModel.user?.userId ?: -1, commentText)
                binding.editTextComment.text.clear()
            } else {
                Toast.makeText(context, "El comentario no puede estar vacío", Toast.LENGTH_SHORT).show()
            }
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