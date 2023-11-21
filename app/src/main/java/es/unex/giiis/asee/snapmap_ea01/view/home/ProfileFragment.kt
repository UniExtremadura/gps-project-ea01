package es.unex.giiis.asee.snapmap_ea01.view.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
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

        setUpUI()
        setUpListeners()

        return binding.root
    }

    private fun setUpUI() {
        val user = requireActivity().intent.getSerializableExtra(HomeActivity.USER_INFO) as es.unex.giiis.asee.snapmap_ea01.data.model.User
        if(user != null){
            binding.tvUsername.text = user.username
            binding.tvAboutMe.text = user.aboutMe
        }
        else Log.d("ProfileFragment", "User is null")
    }

    private fun setUpListeners(){
        with(binding){
            btnEditProfile.setOnClickListener {
                val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                navController.navigate(R.id.action_profileFragment_to_editProfileFragment)
            }
            btnLogout.setOnClickListener{
                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent)
            }
            settingsButton.setOnClickListener{
                val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                navController.navigate(R.id.settingsFragment)
            }
        }
    }
}
