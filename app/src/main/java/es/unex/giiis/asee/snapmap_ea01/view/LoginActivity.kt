package es.unex.giiis.asee.snapmap_ea01.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import es.unex.giiis.asee.snapmap_ea01.data.model.User
import es.unex.giiis.asee.snapmap_ea01.database.SnapMapDatabase
import es.unex.giiis.asee.snapmap_ea01.databinding.ActivityLoginBinding
import es.unex.giiis.asee.snapmap_ea01.utils.CredentialCheck
import es.unex.giiis.asee.snapmap_ea01.view.home.HomeActivity
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var db: SnapMapDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = SnapMapDatabase.getInstance(applicationContext)!!

        setUpListeners()

        //read settings
        readSettings()
    }

    private fun readSettings() {
        val preferences = PreferenceManager.getDefaultSharedPreferences(this).all

        val rememberme = preferences["rememberme"] as Boolean? ?: false
        val username = preferences["username"] as String? ?: ""
        val password = preferences["password"] as String? ?: ""

        if (rememberme) {
            binding.etUsername.setText(username)
            binding.etPassword.setText(password)
        }
    }

    private fun setUpListeners(){
        with(binding) {
            btnLogin.setOnClickListener {
                val check = CredentialCheck.login(
                    etUsername.text.toString().trim(),
                    etPassword.text.toString()
                )
                if(check.fail) notifyInvalidCredentials(check.msg)
                else {
                    checkLogin()
                }
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

    private fun checkLogin(){
        val check = CredentialCheck.login(binding.etUsername.text.toString().trim(),
            binding.etPassword.text.toString())
        if (!check.fail){
            lifecycleScope.launch{
                val user =
                    db?.userDao()?.getUserByUsername(binding.etUsername.text.toString().trim())
                if (user != null) {
                    val check =
                        CredentialCheck.passwordOk(binding.etPassword.text.toString(),
                            user.password)
                    if (check.fail) notifyInvalidCredentials(check.msg)
                    else navigateToHomeActivity(user!!, check.msg)
                }
                else notifyInvalidCredentials("Invalid username")
            }
        }
        else notifyInvalidCredentials(check.msg)
    }
}