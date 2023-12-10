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
import es.unex.giiis.asee.snapmap_ea01.data.model.User
import es.unex.giiis.asee.snapmap_ea01.databinding.FragmentImageBinding

class ImageFragment : Fragment() {

    private lateinit var binding: FragmentImageBinding
    private lateinit var currentUser: User
    private var currentPhotoId: Long = -1

    private val viewModel: ImageViewModel by viewModels { ImageViewModel.Factory }
    private val homeViewModel: HomeViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentImageBinding.inflate(inflater, container, false)
        currentUser = homeViewModel.getCurrentUser()!!
        setUpListeners()
        return binding.root
    }

    private fun configureUI(photo: Photo) {
        with(binding) {
            Glide.with(requireContext())
                .load(photo.photoURL)
                .placeholder(R.drawable.baseline_access_time_24)
                .into(ivImage)

            viewModel.getOwnerPhoto(currentPhotoId)

            tvAuthor.text = "from ${viewModel.ownerUsername}"

            viewModel.isLiked.observe(viewLifecycleOwner) { isLiked ->
                // Actualizar la interfaz de usuario según el estado de "like"
                if (photo != null) {
                    val likeColor = if (isLiked) R.color.like else R.color.white
                    val newColor = ContextCompat.getColor(requireContext(), likeColor)
                    ivLike.setColorFilter(newColor)
                }
            }
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentUser = homeViewModel.getCurrentUser()!!
        if (currentUser != null) {
            viewModel.user = currentUser

            val args = ImageFragmentArgs.fromBundle(requireArguments())
            currentPhotoId = args.photoId
            viewModel.getPhoto(currentPhotoId)

            viewModel.photoState.observe(viewLifecycleOwner) { photo ->
                photo?.let { configureUI(it) }
            }
        }
    }

    private fun setUpListeners() {
        with(binding) {
            ivLike.setOnClickListener {
                viewModel.changeLikeStatus(currentPhotoId, homeViewModel.getCurrentUser())
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