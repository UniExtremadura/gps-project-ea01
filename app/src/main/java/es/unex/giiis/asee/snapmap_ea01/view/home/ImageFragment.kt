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
import es.unex.giiis.asee.snapmap_ea01.database.SnapMapDatabase
import es.unex.giiis.asee.snapmap_ea01.databinding.FragmentImageBinding
import kotlinx.coroutines.launch

class ImageFragment : Fragment() {

    private lateinit var binding: FragmentImageBinding
    private lateinit var db: SnapMapDatabase
    private lateinit var photo: Photo

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentImageBinding.inflate(inflater, container, false)

        db = SnapMapDatabase.getInstance(requireContext())!!
        /*
        val view = inflater.inflate(R.layout.fragment_image, container, false)

        // Obtenemos la referencia de las ImageView ivLike e ivComment
        val likeImageView: ImageView = view.findViewById(R.id.ivLike)
        val commentImageView: ImageView = view.findViewById(R.id.ivComment)

        // Agregamos OnClickListener a la ImageView ivLike
        likeImageView.setOnClickListener {
            val newColor = ContextCompat.getColor(requireContext(), R.color.like)
            likeImageView.setColorFilter(newColor)
        }

        // Agregamos OnClickListener a la ImageView ivComment
        commentImageView.setOnClickListener {
            // Navegamos a CommentsFragment cuando se hace clic en ivComment
            val commentsFragment = CommentsFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, commentsFragment)
                .addToBackStack(null)
                .commit()
        }
        */
        setUpUI()
        setUpListeners()
        return binding.root
    }

    private fun setUpUI() {
        with(binding){
            val args = ImageFragmentArgs.fromBundle(requireArguments())
            val photoId = args.photoId

            lifecycleScope.launch {

                photo = db.photoDao().getPhoto(photoId)

                Glide.with(requireContext())
                    .load(photo.photoURL)
                    .placeholder(R.drawable.baseline_access_time_24)
                    .into(ivImage)

                tvAuthor.text = "from ${db.userDao().getUserById(photo.owner!!)}"
            }
        }
    }

    private fun setUpListeners() {
        with(binding){
            ivLike.setOnClickListener {
                val newColor = ContextCompat.getColor(requireContext(), R.color.like)
                ivLike.setColorFilter(newColor)
            }
            ivComment.setOnClickListener {
                val commentsFragment = CommentsFragment()
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, commentsFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    companion object {
        fun newInstance(): ImageFragment {
            return ImageFragment()
        }
    }
}