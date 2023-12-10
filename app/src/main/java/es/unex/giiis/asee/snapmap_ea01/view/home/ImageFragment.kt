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
    private var currentPhotoId: Long = -1

    private val viewModel: ImageViewModel by viewModels { ImageViewModel.Factory }
    private val homeViewModel: HomeViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentImageBinding.inflate(inflater, container, false)
        val args = ImageFragmentArgs.fromBundle(requireArguments())
        currentPhotoId = args.photoId
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.user.observe(viewLifecycleOwner) { user ->
            viewModel.user = user
        }

        // Obtenemos la foto que queremos ver en la UI
        viewModel.getPhoto(currentPhotoId)

        // Obtenemos el nombre del propietario de la foto
        viewModel.getOwnerPhoto(currentPhotoId)

        viewModel.photoState.observe(viewLifecycleOwner) { photo ->
            photo?.let { configureUI(it) }
        }

        // Observamos el LiveData del nombre del propietario para actualizar la UI
        viewModel.ownerUsername.observe(viewLifecycleOwner) { username ->
            binding.tvAuthor.text = "from $username"
        }

        // Inicializamos el estado del botón de "like"
        viewModel.isLiked.observe(viewLifecycleOwner) { isLiked ->
            updateLikeButton(isLiked)
        }

        // Obtenemos el estado actual del "like" de la base de datos
        viewModel.isLiked(currentPhotoId, homeViewModel.user.value)

        setUpListeners()
    }


    private fun configureUI(photo: Photo) {
        with(binding) {
            Glide.with(requireContext())
                .load(photo.photoURL)
                .placeholder(R.drawable.baseline_access_time_24)
                .into(ivImage)
        }
    }

    private fun setUpListeners() {
        with(binding) {
            ivLike.setOnClickListener {
                    viewModel.changeLikeStatus(currentPhotoId, viewModel.user)
            }

            ivComment.setOnClickListener {
                val commentsFragment = CommentsFragment.newInstance(currentPhotoId)
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, commentsFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

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