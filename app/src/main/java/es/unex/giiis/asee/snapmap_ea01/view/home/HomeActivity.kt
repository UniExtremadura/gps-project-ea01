package es.unex.giiis.asee.snapmap_ea01.view.home


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import es.unex.giiis.asee.snapmap_ea01.R
import es.unex.giiis.asee.snapmap_ea01.data.model.User
import es.unex.giiis.asee.snapmap_ea01.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    private lateinit var homeFragment: HomeFragment
    private lateinit var searchFragment: SearchFragment
    private lateinit var cameraFragment: CameraFragment
    private lateinit var profileFragment: ProfileFragment

    private val viewModel: HomeViewModel by viewModels()

    companion object {
        const val USER_INFO = "USER_INFO"
        fun start(
            context: Context,
            user: User,
        ) {
            val intent = Intent(context, HomeActivity::class.java).apply {
                putExtra(USER_INFO, user)
            }
            context.startActivity(intent)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.userInSession = intent.getSerializableExtra(USER_INFO) as User

        setUpUI()
        setUpListeners()

    }

    // fun setUpUI(user: User) cabecera para cuando tengamos login
    private fun setUpUI() {

        homeFragment = HomeFragment()
        searchFragment = SearchFragment()
        cameraFragment = CameraFragment()
        profileFragment = ProfileFragment()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        binding.bottomNavigation.setupWithNavController(navHostFragment.navController)

        navHostFragment.navController.addOnDestinationChangedListener { _, destination, _ ->
            if ((destination.id == R.id.commentsFragment) ||
                (destination.id == R.id.settingsFragment) ||
                (destination.id == R.id.editProfileFragment) ||
                (destination.id == R.id.followersFragment) ||
                (destination.id == R.id.followingFragment) ||
                (destination.id == R.id.imageFragment) ||
                (destination.id == R.id.tabsFollowFragment)){
                binding.bottomNavigation.visibility = View.GONE
            } else {
                binding.bottomNavigation.visibility = View.VISIBLE
            }
        }

    }

    private fun setUpListeners() {

    }

}