package es.unex.giiis.asee.snapmap_ea01.view.home

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import es.unex.giiis.asee.snapmap_ea01.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
}