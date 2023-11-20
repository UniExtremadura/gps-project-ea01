package es.unex.giiis.asee.snapmap_ea01.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import es.unex.giiis.asee.snapmap_ea01.R
import es.unex.giiis.asee.snapmap_ea01.data.model.Photo
import es.unex.giiis.asee.snapmap_ea01.data.model.User
import es.unex.giiis.asee.snapmap_ea01.data.model.UserPhotoLikeRef
import es.unex.giiis.asee.snapmap_ea01.database.SnapMapDatabase
import es.unex.giiis.asee.snapmap_ea01.databinding.FragmentImageBinding
import kotlinx.coroutines.launch

private const val ARG_PHOTO_ID = "photoId"

class ImageFragment : Fragment() {

    private lateinit var binding: FragmentImageBinding
    private lateinit var db: SnapMapDatabase
    private lateinit var photo: Photo
    private lateinit var currentUser: User
    private var currentPhotoId: Long = -1
    private var like: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentImageBinding.inflate(inflater, container, false)

        db = SnapMapDatabase.getInstance(requireContext())!!

        // Obtiene el objeto User de la actividad anterior
        currentUser =
            requireActivity().intent.getSerializableExtra(HomeActivity.USER_INFO) as User

        setUpUI()
        setUpListeners()
        return binding.root
    }

    private fun setUpUI() {
        with(binding) {
            val args = ImageFragmentArgs.fromBundle(requireArguments())
            currentPhotoId = args.photoId

            lifecycleScope.launch {
                photo = db.photoDao().getPhoto(currentPhotoId)

                Glide.with(requireContext())
                    .load(photo.photoURL)
                    .placeholder(R.drawable.baseline_access_time_24)
                    .into(ivImage)

                // Utiliza el ID del usuario del objeto User
                tvAuthor.text = "from ${db.userDao().getUserById(currentUser.userId)}"

                like = db.userPhotoLikeRefDao().likeExists(currentUser.userId!!, currentPhotoId) > 0
                // Cambia el color del botón like según la existencia de la referencia
                if (like) {
                    val newColor = ContextCompat.getColor(requireContext(), R.color.like)
                    ivLike.setColorFilter(newColor)
                }
            }
        }
    }

    private fun setUpListeners() {
        with(binding) {
            ivLike.setOnClickListener {
                if (like) {
                    lifecycleScope.launch {
                        val userPhotoLikeRef = UserPhotoLikeRef(currentUser.userId!!, currentPhotoId)
                        db.userPhotoLikeRefDao().deletePhotoLikeRef(userPhotoLikeRef)
                        like = false
                        val defaultColor = ContextCompat.getColor(requireContext(), R.color.white)
                        ivLike.setColorFilter(defaultColor)
                    }
                } else {
                    lifecycleScope.launch {
                        val userPhotoLikeRef =
                            currentUser.userId?.let { it1 -> UserPhotoLikeRef(it1, currentPhotoId) }
                        if (userPhotoLikeRef != null) {
                            db.userPhotoLikeRefDao().insertUserPhotoLikeRef(userPhotoLikeRef)
                            like = true
                            val newColor = ContextCompat.getColor(requireContext(), R.color.like)
                            ivLike.setColorFilter(newColor)
                        }
                    }
                }
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
