package com.codeseyee.cloydcontact

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.codeseyee.cloydcontact.ViewModel.MainViewModel
import com.codeseyee.cloydcontact.fragments.ContactsFragment
import com.codeseyee.cloydcontact.fragments.FavouriteFragment
import com.codeseyee.cloydcontact.fragments.MainFragment
import com.codeseyee.cloydcontact.fragments.NearByFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userId = intent.getStringExtra("USER_ID")
        val userType = intent.getStringExtra("USER_TYPE") ?: "individual" // Default to "individual"

        bottomNav = findViewById(R.id.idbottom_nav)
        drawerLayout = findViewById(R.id.drawer_layout)
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val drawerIcon: View = findViewById(R.id.drawer_icon)
        drawerIcon.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.idbottomhome -> selectTab(0, userId, userType)
                R.id.idbottomnearby -> selectTab(1, userId, userType)
                R.id.idbottompinned -> selectTab(2, userId, userType)
                R.id.idbottommyrecent -> selectTab(3, userId, userType)
            }
            true
        }
        bottomNav.selectedItemId = R.id.idbottomhome

        observeViewModel()
        setupDrawerHeader(userId, userType)
    }

    private fun observeViewModel() {
        viewModel.selectedFragmentIndex.observe(this) { index ->
            intent.getStringExtra("USER_TYPE")
                ?.let { selectTab(index, intent.getStringExtra("USER_ID"), it) }
        }
    }

    private fun selectTab(index: Int, userId: String?, userType: String) {
        val fragment = when (index) {
            0 -> ContactsFragment().apply {
                arguments = Bundle().apply {
                    putString("USER_ID", userId)
                    putString("USER_TYPE", userType)
                }
            }
            1 -> FavouriteFragment()
            2 -> NearByFragment()
            3 -> MainFragment()
            else -> ContactsFragment()
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, fragment)
            .commit()
    }

    private fun setupDrawerHeader(userId: String?, userType: String) {
        val navView: NavigationView = findViewById(R.id.nav_view)
        val headerView: View = navView.getHeaderView(0)

        val nameTextView: TextView = headerView.findViewById(R.id.name)
        val emailTextView: TextView = headerView.findViewById(R.id.email)
        val imageView: ShapeableImageView = headerView.findViewById(R.id.image)

        userId?.let {
            val firestore = FirebaseFirestore.getInstance()
            val collection = if (userType == "individual") "individual" else "business"

            firestore.collection(collection).document(it).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val name = document.getString("name") ?: "No name"
                        val email = document.getString("email") ?: "No email"
                        val profileImageUrl = document.getString("profileImage")

                        nameTextView.text = name
                        emailTextView.text = email
                        // Load the profile image using Glide
                        Glide.with(this)
                            .load(profileImageUrl)
                            .placeholder(R.drawable.placeholder) // Optional placeholder
                            .error(R.drawable.placeholder) // Optional error image
                            .into(imageView)
                    } else {
                        // Handle the case where the document does not exist
                        nameTextView.text = "No name"
                        emailTextView.text = "No email"
                        imageView.setImageResource(R.drawable.placeholder) // Optional placeholder
                    }
                }
                .addOnFailureListener {
                    // Handle errors
                    nameTextView.text = "Error"
                    emailTextView.text = "Error"
                    imageView.setImageResource(R.drawable.placeholder) // Optional placeholder
                }
        }
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
        // Implement logout functionality here
    }
}
