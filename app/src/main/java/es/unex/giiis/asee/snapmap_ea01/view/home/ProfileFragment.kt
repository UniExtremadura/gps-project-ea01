package es.unex.giiis.asee.snapmap_ea01.view.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import es.unex.giiis.asee.snapmap_ea01.R
import es.unex.giiis.asee.snapmap_ea01.databinding.FragmentProfileBinding
import es.unex.giiis.asee.snapmap_ea01.view.LoginActivity


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding  // Declara una variable de enlace

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        setUpListeners()

        return binding.root
    }

    private fun setUpUI() {

    }

    private fun setUpListeners(){
        with(binding){
            btnEditProfile.setOnClickListener {
                val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                navController.navigate(R.id.editProfileFragment)
            }
            btnLogout.setOnClickListener{
                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
