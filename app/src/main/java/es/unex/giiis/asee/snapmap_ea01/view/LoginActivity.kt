package es.unex.giiis.asee.snapmap_ea01.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import es.unex.giiis.asee.snapmap_ea01.databinding.ActivityLoginBinding
import es.unex.giiis.asee.snapmap_ea01.model.User
import es.unex.giiis.asee.snapmap_ea01.utils.CredentialCheck
import es.unex.giiis.asee.snapmap_ea01.view.home.HomeActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpListeners()
    }

    private fun setUpListeners(){
        with(binding) {
            btnLogin.setOnClickListener {
                val check = CredentialCheck.login(
                    etUsername.text.toString(),
                    etPassword.text.toString()
                )
                if(check.fail) notifyInvalidCredentials(check.msg)
                else navigateToHomeActivity(User("username", "about me",
                    etUsername.text.toString(), etPassword.text.toString()),check.msg)
            }
            btnRegister.setOnClickListener {
                intent = Intent(this@LoginActivity, JoinActivity::class.java)
                startActivity(intent)
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