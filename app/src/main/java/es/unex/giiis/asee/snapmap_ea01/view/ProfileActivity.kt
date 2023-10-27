package es.unex.giiis.asee.snapmap_ea01.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import es.unex.giiis.asee.snapmap_ea01.databinding.ActivityProfileBinding
import es.unex.giiis.asee.snapmap_ea01.model.User

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    companion object {
        const val USER_INFO = "USER_INFO"
        fun start(
            context: Context,
            user: User,
        ) {
            val intent = Intent(context, ProfileActivity::class.java).apply {
                putExtra(USER_INFO, user)
            }
            context.startActivity(intent)
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = intent.getSerializableExtra(USER_INFO) as User
        setUpUI(user)
    }

    private fun setUpUI(user: User) {
        //User binding to layout items
        with(binding) {
            tvUsername.text = user.username
            tvAboutMe.text = user.aboutMe
        }
    }

    private fun setUpListeners() {

        val context:Context = this

        with(binding) {
            btnEditProfile.setOnClickListener {
            }
            btnLogout.setOnClickListener {
                val intent = Intent(context, JoinActivity::class.java)
                context.startActivity(intent)
            }
        }
    }
}