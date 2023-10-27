package es.unex.giiis.asee.snapmap_ea01.view.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import es.unex.giiis.asee.snapmap_ea01.databinding.FragmentEditProfileBinding
import com.google.android.material.snackbar.Snackbar
import es.unex.giiis.asee.snapmap_ea01.R

class EditProfileFragment : Fragment() {
    private lateinit var binding: FragmentEditProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)

        val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)

        binding.btnEdit.setOnClickListener {
            Snackbar.make(requireView(), "Credentials updated successfully", Snackbar.LENGTH_SHORT).show()
        }

        binding.btnBack.setOnClickListener {
            navController.navigateUp()
        }

        return binding.root
    }
}
