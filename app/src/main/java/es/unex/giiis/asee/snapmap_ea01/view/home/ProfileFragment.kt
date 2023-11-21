package es.unex.giiis.asee.snapmap_ea01.view.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import es.unex.giiis.asee.snapmap_ea01.R
import es.unex.giiis.asee.snapmap_ea01.database.SnapMapDatabase
import es.unex.giiis.asee.snapmap_ea01.databinding.FragmentProfileBinding
import es.unex.giiis.asee.snapmap_ea01.view.LoginActivity
import kotlinx.coroutines.launch


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding  // Declara una variable de enlace
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

        setUpUI()
        setUpListeners()

        return binding.root
    }

    private fun setUpUI() {
        val user = requireActivity().intent.getSerializableExtra(HomeActivity.USER_INFO) as es.unex.giiis.asee.snapmap_ea01.data.model.User
        var followers : Int
        var following : Int
        if(user != null){
            lifecycleScope.launch {

                followers = user.userId?.let { db.userUserFollowRefDao().getFollowers(it).size }!!
                following = user.userId?.let { db.userUserFollowRefDao().getFollowing(it).size }!!

                binding.tvFollowing.text = following.toString()
                binding.tvFollowers.text = followers.toString()
                binding.tvUsername.text = user.username
                binding.tvAboutMe.text = user.aboutMe
            }
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
