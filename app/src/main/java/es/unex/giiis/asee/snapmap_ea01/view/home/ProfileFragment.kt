package es.unex.giiis.asee.snapmap_ea01.view.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import es.unex.giiis.asee.snapmap_ea01.R
import es.unex.giiis.asee.snapmap_ea01.database.SnapMapDatabase
import es.unex.giiis.asee.snapmap_ea01.databinding.FragmentProfileBinding
import es.unex.giiis.asee.snapmap_ea01.view.LoginActivity


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding  // Declara una variable de enlace

    private val viewModel: ProfileViewModel by viewModels { ProfileViewModel.Factory }
    private val homeViewModel: HomeViewModel by activityViewModels()

    private lateinit var db: SnapMapDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = SnapMapDatabase.getInstance(requireContext())!!
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        setUpListeners()

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.user.observe(viewLifecycleOwner) { user ->
            viewModel.user = user
            observeFollowersAndFollowing()
            setUpUI()
        }
    }

    private fun observeFollowersAndFollowing() {
        viewModel.followers.observe(viewLifecycleOwner) { followers ->
            // Actualiza la UI con la lista de seguidores
            val followersCount = followers?.size ?: 0
            binding.tvFollowers.text = followersCount.toString()
        }

        viewModel.following.observe(viewLifecycleOwner) { following ->
            // Actualiza la UI con la lista de usuarios que sigue
            val followingCount = following?.size ?: 0
            binding.tvFollowing.text = followingCount.toString()
        }
    }

    private fun setUpUI() {
        if(viewModel.user != null){

            binding.tvUsername.text = viewModel.user!!.username
            binding.tvAboutMe.text = viewModel.user!!.aboutMe
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
            btnFollowers.setOnClickListener {
                val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                navController.navigate(
                    R.id.action_profileFragment_to_tabsFollowFragment,
                    bundleOf("initialTabIndex" to 0)
                )
            }
            btnFollowing.setOnClickListener {
                val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                navController.navigate(
                    R.id.action_profileFragment_to_tabsFollowFragment,
                    bundleOf("initialTabIndex" to 1)
                )
            }
            settingsButton.setOnClickListener{
                val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                navController.navigate(R.id.settingsFragment)
            }
        }
    }
}