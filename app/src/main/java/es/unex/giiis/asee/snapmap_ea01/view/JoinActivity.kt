package es.unex.giiis.asee.snapmap_ea01.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import es.unex.giiis.asee.snapmap_ea01.databinding.ActivityJoinBinding
import es.unex.giiis.asee.snapmap_ea01.model.User
import es.unex.giiis.asee.snapmap_ea01.utils.CredentialCheck
import es.unex.giiis.asee.snapmap_ea01.view.home.HomeActivity


class JoinActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJoinBinding

    companion object {

        const val USERNAME = "JOIN_USERNAME"
        const val EMAIL = "JOIN_EMAIL"
        const val PASS = "JOIN_PASS"
        const val ABOUT = "JOIN_ABOUT"
        fun start(
            context: Context,
            responseLauncher: ActivityResultLauncher<Intent>
        ) {
            val intent = Intent(context, JoinActivity::class.java)
            responseLauncher.launch(intent)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpListeners()
    }

    private fun setUpListeners(){
        with(binding) {
            btnRegister.setOnClickListener {
                val check = CredentialCheck.join(
                    etUsername.text.toString(),
                    etEmail.text.toString(),
                    etPassword.text.toString(),
                    etAboutMe.text.toString()
                )
                if (check.fail) notifyInvalidCredentials(check.msg)
                else navigateToHomeActivity(User(etUsername.text.toString(), etAboutMe.text.toString(),
                    etEmail.text.toString(),etPassword.text.toString()),check.msg)

            }
            btnLogin.setOnClickListener {
                //TODO: Navigate to LoginActivity
                intent = Intent(this@JoinActivity, LoginActivity::class.java)
                startActivity(intent)
            }
            ivProfilePicture.setOnClickListener {
                //TODO: Select a profile picture
            }

        }
    }

    private fun navigateToHomeActivity(user: User, msg: String) {
        HomeActivity.start(this, user)
    }

    private fun notifyInvalidCredentials(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

}