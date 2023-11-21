package es.unex.giiis.asee.snapmap_ea01.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import es.unex.giiis.asee.snapmap_ea01.data.model.User
import es.unex.giiis.asee.snapmap_ea01.database.SnapMapDatabase
import es.unex.giiis.asee.snapmap_ea01.database.UserDao
import es.unex.giiis.asee.snapmap_ea01.databinding.ActivityJoinBinding
import es.unex.giiis.asee.snapmap_ea01.utils.CredentialCheck
import es.unex.giiis.asee.snapmap_ea01.view.home.HomeActivity
import kotlinx.coroutines.launch


class JoinActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJoinBinding
    private lateinit var db: SnapMapDatabase

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

        //Database inicialization
        db = SnapMapDatabase.getInstance(applicationContext)!!

        setUpListeners()
    }

    private fun setUpListeners(){
        with(binding) {
            btnRegister.setOnClickListener {
                join()
            }
            btnLogin.setOnClickListener {
                val intent = Intent(this@JoinActivity, LoginActivity::class.java)
                startActivity(intent)
            }

        }
    }

    private fun join(){
        with(binding) {
            val check = CredentialCheck.join(
                etUsername.text.toString().trim(),
                etEmail.text.toString(),
                etPassword.text.toString(),
                etAboutMe.text.toString()
            )
            if (check.fail) notifyInvalidCredentials(check.msg)
            else {
                lifecycleScope.launch {
                    val user = User(
                        null,
                        etUsername.text.toString().trim(),
                        etAboutMe.text.toString(),
                        etEmail.text.toString(),
                        etPassword.text.toString()
                    )
                    val id = db.userDao().insertUser(user)

                    navigateToHomeActivity(
                        User(
                            id,
                            etUsername.text.toString().trim(),
                            etAboutMe.text.toString(),
                            etEmail.text.toString(),
                            etPassword.text.toString()
                        ),
                        check.msg
                    )
                }
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