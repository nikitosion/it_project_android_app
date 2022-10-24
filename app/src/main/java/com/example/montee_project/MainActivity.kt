package com.example.montee_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.room.Room
import com.example.montee_project.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Bottom menu init
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setupWithNavController(navController)

        // Toolbar init
        val toolbar: Toolbar = findViewById(R.id.myToolbar)
        toolbar.title = getString(R.string.main_page_label)
        toolbar.navigationIcon = null
        setSupportActionBar(toolbar)
        setupActionBarWithNavController(navController)

        // Bottom menu buttons' actions
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.main_page_button -> {
                    if (supportFragmentManager.fragments.last() !is MainPage) {
                        openFragment(MainPage.newInstance())
                        toolbar.title = getString(R.string.main_page_label)
                    }
                    true
                }
                R.id.search_page_button -> {
                    if (supportFragmentManager.fragments.last() !is SearchPage) {
                        openFragment(SearchPage.newInstance())
                        toolbar.title = getString(R.string.search_page_label)
                    }
                    true
                }
                R.id.add_food_page_button -> {
                    if (supportFragmentManager.fragments.last() !is AddFoodPage) {
                        openFragment(AddFoodPage.newInstance())
                        toolbar.title = getString(R.string.add_food_page_label)
                    }
                    true
                }
                R.id.profile_page_button -> {
                    if (supportFragmentManager.fragments.last() !is ProfilePage) {
                        openFragment(ProfilePage.newInstance("", ""))
                        toolbar.title = getString(R.string.profile_page_label)
                    }
                    true
                }
                R.id.shopping_list_page_button -> {
                    if (supportFragmentManager.fragments.last() !is ShoppingListPage) {
                        openFragment(ShoppingListPage.newInstance())
                        toolbar.title = getString(R.string.shopping_list_label)
                    }
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    // Navigation actions and animation of it
    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(
            androidx.appcompat.R.anim.abc_grow_fade_in_from_bottom,
            androidx.appcompat.R.anim.abc_shrink_fade_out_from_bottom,
            androidx.appcompat.R.anim.abc_grow_fade_in_from_bottom,
            androidx.appcompat.R.anim.abc_shrink_fade_out_from_bottom
        )
        transaction.add(R.id.nav_host_fragment, fragment)
        transaction.commit()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}