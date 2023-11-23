package es.unex.giiis.asee.snapmap_ea01.view.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import es.unex.giiis.asee.snapmap_ea01.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        // Obtener referencias a las preferencias
        val darkModeSwitch: SwitchPreference? = findPreference("darkmode")
        val usernamePreference: EditTextPreference? = findPreference("username")
        val passwordPreference: EditTextPreference? = findPreference("password")

        // Establecer el estado inicial del switch después de configurar el listener
        val systemDefaultMode = when (AppCompatDelegate.getDefaultNightMode()) {
            AppCompatDelegate.MODE_NIGHT_YES -> true
            else -> false
        }
        darkModeSwitch?.isChecked = systemDefaultMode

        darkModeSwitch?.setOnPreferenceChangeListener { _, newValue ->
            val isDarkModeEnabled = newValue as Boolean
            if (isDarkModeEnabled) {
                // Habilitar el modo noche
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                // Habilitar el modo día
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }

            // Recrear la actividad para aplicar el cambio de tema
            activity?.recreate()

            true
        }

        // Obtener los valores guardados en SharedPreferences y establecerlos en los EditTextPreference
        val sharedPreferences = preferenceManager.sharedPreferences
        val savedUsername = sharedPreferences?.getString("username", "Default username")
        val savedPassword = sharedPreferences?.getString("password", "Default password")

        usernamePreference?.summary = savedUsername
        passwordPreference?.summary = savedPassword
    }
}
