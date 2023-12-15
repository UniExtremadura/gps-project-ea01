package es.unex.giiis.asee.snapmap_ea01.view.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData

class SettingsViewModel : ViewModel() {

    val darkModeEnabled = MutableLiveData<Boolean>()
    val rememberMeEnabled = MutableLiveData<Boolean>()
    val username = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    fun updateDarkMode(isDarkModeEnabled: Boolean) {
        darkModeEnabled.value = isDarkModeEnabled
    }

    fun updateUsernameAndPassword(username: String, password: String) {
        this.username.value = username
        this.password.value = password
    }

    fun updateRememberMe(isRememberMeEnabled: Boolean) {
        rememberMeEnabled.value = isRememberMeEnabled
    }
}
