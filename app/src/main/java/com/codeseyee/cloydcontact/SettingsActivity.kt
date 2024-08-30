package com.codeseyee.cloydcontact

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.codeseyee.cloydcontact.viewmodels.SettingsViewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportActionBar?.title = "Settings"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, SettingsFragment())
            .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        private lateinit var viewModel: SettingsViewModel

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            viewModel = ViewModelProvider(requireActivity()).get(SettingsViewModel::class.java)

            findPreference<Preference>("deactivate")?.setOnPreferenceClickListener {
                // Implement your deactivate account logic here
                viewModel.deactivateAccount()
                true
            }

            findPreference<Preference>("delete")?.setOnPreferenceClickListener {
                // Implement your delete account logic here
                startActivity(Intent(requireContext(), DeletePermanentlyActivity::class.java))
                true
            }

            findPreference<Preference>("logout")?.setOnPreferenceClickListener {
                // Implement your logout logic here
                viewModel.logout()
                true
            }
        }
    }
}
