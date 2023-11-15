package es.unex.giiis.asee.snapmap_ea01.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import es.unex.giiis.asee.snapmap_ea01.R

class ImageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        return view
    }

    companion object {
        fun newInstance(): ImageFragment {
            return ImageFragment()
        }
    }
}