package es.unex.giiis.asee.snapmap_ea01.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import es.unex.giiis.asee.snapmap_ea01.databinding.FragmentProfileBinding
import androidx.navigation.NavController
import androidx.navigation.Navigation
import es.unex.giiis.asee.snapmap_ea01.R

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding  // Declara una variable de enlace

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        binding.btnEditProfile.setOnClickListener {
            navController.navigate(R.id.editProfileFragment)
        }
        return binding.root
    }
}
