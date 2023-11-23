package es.unex.giiis.asee.snapmap_ea01.view.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import es.unex.giiis.asee.snapmap_ea01.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)



        val darkModeSwitch: SwitchPreference? = findPreference("darkmode")

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

    }
}
