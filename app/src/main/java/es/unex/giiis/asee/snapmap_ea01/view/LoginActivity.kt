package es.unex.giiis.asee.snapmap_ea01.view

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import es.unex.giiis.asee.snapmap_ea01.R
import es.unex.giiis.asee.snapmap_ea01.api.getNetworkService
import es.unex.giiis.asee.snapmap_ea01.data.Repository
import es.unex.giiis.asee.snapmap_ea01.data.model.User
import es.unex.giiis.asee.snapmap_ea01.database.SnapMapDatabase
import es.unex.giiis.asee.snapmap_ea01.databinding.ActivityLoginBinding
import es.unex.giiis.asee.snapmap_ea01.utils.CredentialCheck
import es.unex.giiis.asee.snapmap_ea01.view.home.HomeActivity
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var repository: Repository
    private lateinit var db: SnapMapDatabase

    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen()

        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = SnapMapDatabase.getInstance(applicationContext)!!

        repository = Repository.getInstance(db!!.userDao(), db.userUserFollowRefDao(), db.userPhotoLikeRefDao(), db.commentDao(), db.photoDao(), db.photoURIDao(),getNetworkService())

        setUpListeners()

        readSettings()

    }

    private fun readSettings() {
        val preferences = PreferenceManager.getDefaultSharedPreferences(this).all

        val isLoggedIn = preferences["isLoggedIn"] as Boolean? ?: false
        if (isLoggedIn) {
            val username = preferences["username"] as String? ?: ""
            lifecycleScope.launch {
                val user = repository.getUserByUsername(username)
                user?.let { navigateToHomeActivity(it, "Welcome back!") }
            }
            finish()
        } else {
            val rememberme = preferences["rememberme"] as Boolean? ?: false
            val username = preferences["username"] as String? ?: ""
            val password = preferences["password"] as String? ?: ""
            val s = preferences["darkmode"] as Boolean? ?: false

            if (rememberme) {
                binding.etUsername.setText(username)
                binding.etPassword.setText(password)
            }
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
                    repository.getUserByUsername(binding.etUsername.text.toString().trim())
                if (user != null) {
                    val check = CredentialCheck.passwordOk(binding.etPassword.text.toString(), user.password)
                    if (check.fail) {
                        notifyInvalidCredentials(check.msg)
                    }
                    else {
                        // Guardar el nombre de usuario y contraseña en SharedPreferences
                        saveUserCredentials(binding.etUsername.text.toString().trim(), binding.etPassword.text.toString())

                        saveLoginState(true)

                        navigateToHomeActivity(user, check.msg)
                        finish()
                    }
                }
                else notifyInvalidCredentials("Invalid username")
            }
        }
        else notifyInvalidCredentials(check.msg)
    }

    private fun saveLoginState(isLoggedIn: Boolean) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = preferences.edit()
        editor.putBoolean("isLoggedIn", isLoggedIn)
        editor.apply()
    }

    private fun saveUserCredentials(username: String, password: String) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = preferences.edit()
        editor.putString("username", username)
        editor.putString("password", password)
        editor.apply()
    }
}