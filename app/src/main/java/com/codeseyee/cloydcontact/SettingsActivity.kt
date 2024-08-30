package com.codeseyee.cloydcontact

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.codeseyee.cloudcontacts.viewmodels.SettingsViewModel
import com.codeseyee.cloydcontact.utils.AppUtils
import com.codeseyee.cloydcontact.utils.PreferenceManager

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

            findPreference<Preference>("profile")?.setOnPreferenceClickListener {
                AppUtils.launchProfileEditActivity(requireContext())
                true
            }

//            findPreference<Preference>("changePassword")?.let { changePasswordPref ->
//                val currentAuthProvider = PreferenceManager.getCurrentUser()?.authProvider
//                if (currentAuthProvider != "email") {
//                    changePasswordPref.isVisible = false
//                }
//            }

            findPreference<Preference>("deactive")?.setOnPreferenceClickListener {
                AppUtils.showYesNoDialog(
                    requireContext(),
                    "Sure to deactivate?",
                    "Are you sure you want to deactivate your account? You can login again to activate your account."
                ) {
                    viewModel.deactivateAccount()
                }
                true
            }

            findPreference<Preference>("delete")?.setOnPreferenceClickListener {
                AppUtils.showYesNoDialog(
                    requireContext(),
                    "Sure to delete?",
                    "Are you sure you want to delete your account?"
                ) {
                    startActivity(Intent(requireContext(), DeletePermanentlyActivity::class.java))
                }
                true
            }

            findPreference<Preference>("logout")?.setOnPreferenceClickListener {
                AppUtils.showYesNoDialog(
                    requireContext(),
                    "Sure to logout?",
                    "Are you sure you want to logout?"
                ) {
                    viewModel.logout()
                }
                true
            }
        }
    }
}
