package es.unex.giiis.asee.snapmap_ea01.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import es.unex.giiis.asee.snapmap_ea01.R
import es.unex.giiis.asee.snapmap_ea01.data.model.Photo
import es.unex.giiis.asee.snapmap_ea01.databinding.FragmentImageBinding

class ImageFragment : Fragment() {

    private lateinit var binding: FragmentImageBinding
    private var currentPhotoId: Long = -1 // variable que va a ser usada para saber el ID de la foto actual

    // ViewModel para la lógica relacionada con las imágenes
    private val viewModel: ImageViewModel by viewModels { ImageViewModel.Factory }
    // ViewModel para la lógica relacionada con la pantalla principal. Se va a usar para recuperar al usuario actual
    private val homeViewModel: HomeViewModel by activityViewModels()

    // Infla la vista del fragmento y obtiene el ID de la foto de los argumentos
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentImageBinding.inflate(inflater, container, false)
        val args = ImageFragmentArgs.fromBundle(requireArguments())
        currentPhotoId = args.photoId
        return binding.root
    }

    // Configuración de la vista una vez que se ha creado
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observa el usuario actual desde el ViewModel de la pantalla principal
        homeViewModel.user.observe(viewLifecycleOwner) { user ->
            viewModel.user = user
        }

        // Obtiene la foto y su información para mostrar en la UI
        viewModel.getPhoto(currentPhotoId)
        viewModel.getOwnerPhoto(currentPhotoId)

        // Observa el estado de la foto para actualizar la UI cuando cambie
        viewModel.photoState.observe(viewLifecycleOwner) { photo ->
            photo?.let { configureUI(it) }
        }

        // Observa el nombre del propietario de la foto para actualizar la UI
        viewModel.ownerUsername.observe(viewLifecycleOwner) { username ->
            binding.tvAuthor.text = "from $username"
        }

        // Inicializa y observa el estado del botón de "me gusta"
        viewModel.isLiked.observe(viewLifecycleOwner) { isLiked ->
            updateLikeButton(isLiked)
        }

        // Obtiene el estado actual del "me gusta" de la base de datos
        viewModel.isLiked(currentPhotoId, homeViewModel.user.value)

        // Configura los listeners para los botones de "me gusta" y comentarios
        setUpListeners()
    }

    // Configura la UI con la información de la foto
    private fun configureUI(photo: Photo) {
        with(binding) {
            Glide.with(requireContext())
                .load(photo.photoURL)
                .placeholder(R.drawable.baseline_access_time_24)
                .into(ivImage)
        }
    }

    // Configura los listeners para los botones
    private fun setUpListeners() {
        with(binding) {
            // Listener para el botón de "me gusta"
            ivLike.setOnClickListener {
                viewModel.changeLikeStatus(currentPhotoId, viewModel.user)
            }

            // Listener para el botón de comentarios
            ivComment.setOnClickListener {
                // Navega al fragmento de comentarios si pulsamos sobre dicho botón
                val commentsFragment = CommentsFragment.newInstance(currentPhotoId)
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, commentsFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    // Actualiza el color del botón de "me gusta" según si la foto está marcada como "me gusta" o no
    private fun updateLikeButton(isLiked: Boolean) {
        val likeColor = if (isLiked) R.color.like else R.color.white
        val newColor = ContextCompat.getColor(requireContext(), likeColor)
        binding.ivLike.setColorFilter(newColor)
    }

    companion object {
        fun newInstance(photoId: Long): ImageFragment {
            return ImageFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_PHOTO_ID, photoId)
                }
            }
        }
    }
}