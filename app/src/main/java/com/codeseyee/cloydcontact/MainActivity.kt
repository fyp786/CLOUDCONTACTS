package com.codeseyee.cloydcontact


import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.codeseyee.cloydcontact.Repository.ContactRepository
import com.codeseyee.cloydcontact.fragments.ContactsFragment
import com.codeseyee.cloydcontact.fragments.FavouriteFragment
import com.codeseyee.cloydcontact.fragments.MainFragment
import com.codeseyee.cloydcontact.fragments.NearByFragment
import com.codeseyee.cloydcontact.ViewModel.MainViewModel
import com.codeseyee.cloydcontact.factories.MainViewModelFactory
import com.codeseyee.cloydcontact.room.AppDatabase
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(ContactRepository(AppDatabase.getDatabase(this).contactDao()))
    }
    private lateinit var bottomNav: BottomNavigationView
    private val fragmentList: List<Fragment> = listOf(
        ContactsFragment(),
        FavouriteFragment(),
        NearByFragment(),
        MainFragment()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNav = findViewById(R.id.idbottom_nav)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.idbottomhome -> selectTab(0)
                R.id.idbottomnearby -> selectTab(1)
                R.id.idbottompinned -> selectTab(2)
                R.id.idbottommyrecent -> selectTab(3)
            }
            true
        }
        bottomNav.selectedItemId = R.id.idbottomhome

        viewModel.loadContacts()
        viewModel.deleteOldContacts()
    }

    private fun selectTab(index: Int) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, fragmentList[index])
            .commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks
        when (item.itemId) {
            R.id.nav_logout -> logout()
            R.id.nav_setting -> startActivity(Intent(this, SettingsActivity::class.java))
            // Handle other navigation items
        }
        findViewById<DrawerLayout>(R.id.drawer_layout).closeDrawers()
        return true
    }

    private fun logout() {
        // Handle logout
    }
}
