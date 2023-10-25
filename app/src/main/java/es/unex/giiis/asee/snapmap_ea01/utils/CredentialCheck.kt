package es.unex.giiis.asee.snapmap_ea01.utils

import android.util.Patterns

class CredentialCheck private constructor(){

    var fail: Boolean = false
    var msg: String = ""
    var error: CredentialError = CredentialError.PasswordError

    companion object{

        private val TAG = CredentialCheck::class.java.canonicalName
        private val MINCHARS = 4

        private val checks = arrayOf(
            CredentialCheck().apply {
                fail = false
                msg = "Your credentials are OK"
                error = CredentialError.Success
            },
            CredentialCheck().apply {
                fail = true
                msg = "Invalid username"
                error = CredentialError.UsernameError
            },
            CredentialCheck().apply {
                fail = true
                msg = "Invalid password"
                error = CredentialError.PasswordError
            },
            CredentialCheck().apply {
                fail = true
                msg = "Invalid email"
                error = CredentialError.EmailError
            },
            CredentialCheck().apply {
                fail = true
                msg = "Invalid about me"
                error = CredentialError.AboutMeError
            }
        )

        fun login(username: String, password: String): CredentialCheck {
            return if (username.isBlank() || username.length < MINCHARS) checks[1]
            else if (password.isBlank() || password.length < MINCHARS) checks[2]
            else checks[0]
        }

        fun join(username: String, email: String, password: String, aboutMe: String): CredentialCheck {
            return if (username.isBlank() || username.length < MINCHARS) checks[1]
            else if (password.isBlank() || password.length < MINCHARS) checks[2]
            else if (email.isBlank() || email.length < MINCHARS || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) checks[3]
            else if (aboutMe.isBlank() || aboutMe.length < MINCHARS) checks[4]
            else checks[0]
        }
    }

    enum class CredentialError {
        PasswordError, UsernameError, EmailError, AboutMeError, Success
    }
}
