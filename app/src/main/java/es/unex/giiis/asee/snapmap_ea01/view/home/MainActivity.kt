package es.unex.giiis.asee.snapmap_ea01.view.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import es.unex.giiis.asee.snapmap_ea01.R
import es.unex.giiis.asee.snapmap_ea01.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var homeFragment: HomeFragment
    private lateinit var searchFragment: SearchFragment
    private lateinit var cameraFragment: CameraFragment
    private lateinit var profileFragment: ProfileFragment

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpUI()
        //setUpListeners()

    }

    // Comentario

    // fun setUpUI(user: User) cabecera para cuando tengamos login
    fun setUpUI() {

        homeFragment = HomeFragment()
        searchFragment = SearchFragment()
        cameraFragment = CameraFragment()
        profileFragment = ProfileFragment()

        setCurrentFragment(homeFragment)

    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
            commit()
        }

    fun setUpListeners() {
//nothing to do
        with(binding){
            bottomNavigation.setOnItemSelectedListener {
                when(it.itemId){
                    R.id.action_home -> setCurrentFragment(homeFragment)
                    R.id.action_search -> setCurrentFragment(searchFragment)
                    R.id.action_camera -> setCurrentFragment(cameraFragment)
                    R.id.action_profile -> setCurrentFragment(profileFragment)

                    else -> setCurrentFragment(homeFragment)
                }
                true
            }
        }
    }

}