package com.codeseyee.cloydcontact

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.codeseyee.cloydcontact.fragments.ContactsFragment
import com.codeseyee.cloydcontact.fragments.FavouriteFragment
import com.codeseyee.cloydcontact.fragments.MainFragment
import com.codeseyee.cloydcontact.fragments.NearByFragment
import com.codeseyee.cloydcontact.ViewModel.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
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
        drawerLayout = findViewById(R.id.drawer_layout)
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

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

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.selectedFragmentIndex.observe(this) { index ->
            selectTab(index)
        }
    }

    private fun selectTab(index: Int) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, fragmentList[index])
            .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (toggle.onOptionsItemSelected(item)) {
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_logout -> logout()
            R.id.nav_setting -> startActivity(Intent(this, SettingsActivity::class.java))
        }
        drawerLayout.closeDrawers()
        return true
    }

    private fun logout() {
    }
}
