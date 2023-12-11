package es.unex.giiis.asee.snapmap_ea01.view.home

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import es.unex.giiis.asee.snapmap_ea01.R

class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        val darkModeSwitch: SwitchPreference? = findPreference("darkmode")
        val rememberMeSwitch: SwitchPreference? = findPreference("rememberme")

        val systemDefaultMode = when (AppCompatDelegate.getDefaultNightMode()) {
            AppCompatDelegate.MODE_NIGHT_YES -> true
            else -> false
        }
        darkModeSwitch?.isChecked = systemDefaultMode

        darkModeSwitch?.setOnPreferenceChangeListener { _, newValue ->
            val isDarkModeEnabled = newValue as Boolean
            viewModel.updateDarkMode(isDarkModeEnabled)
            applyTheme(isDarkModeEnabled)
            true
        }

        rememberMeSwitch?.setOnPreferenceChangeListener { _, newValue ->
            val rememberMeEnabled = newValue as Boolean
            viewModel.updateRememberMe(rememberMeEnabled)
            true
        }

        // Observar los cambios en el ViewModel
        viewModel.username.observe(this, Observer { username ->
            findPreference<EditTextPreference>("username")?.summary = username
        })

        viewModel.password.observe(this, Observer { password ->
            findPreference<EditTextPreference>("password")?.summary = password
        })

        // Obtener los valores guardados en SharedPreferences y establecerlos en los EditTextPreference
        updateUsernameAndPassword(rememberMeSwitch?.isChecked)
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences?.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences?.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == "rememberme") {
            updateUsernameAndPassword(sharedPreferences?.getBoolean(key, false))
        }
    }

    private fun applyTheme(isDarkModeEnabled: Boolean) {
        if (isDarkModeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        activity?.recreate()
    }

    private fun updateUsernameAndPassword(rememberMeEnabled: Boolean?) {
        if (rememberMeEnabled == true) {
            val sharedPreferences = preferenceManager.sharedPreferences
            val savedUsername = sharedPreferences?.getString("username", "Default username")
            val savedPassword = sharedPreferences?.getString("password", "Default password")

            viewModel.updateUsernameAndPassword(savedUsername ?: "", savedPassword ?: "")
        } else {
            viewModel.updateUsernameAndPassword("Default username", "Default password")
        }
    }
}
