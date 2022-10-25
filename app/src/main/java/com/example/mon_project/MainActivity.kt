package com.example.mon_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.mon_project.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

const val TAG = "MainActivityChecker"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

<<<<<<< HEAD:app/src/main/java/com/example/mon_project/MainActivity.kt
        bottomNavigationView.setupWithNavController(navController)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
=======
        // Инициализируем нижнее меню
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setupWithNavController(navController)

        // Инициализируем toolbar
        val toolbar: Toolbar = findViewById(R.id.myToolbar)
>>>>>>> bd20ec8 (Bug fixes. SearchPage was implemented):app/src/main/java/com/example/montee_project/MainActivity.kt
        toolbar.title = getString(R.string.main_page_label)
        toolbar.navigationIcon = null
        setSupportActionBar(toolbar)

        setupActionBarWithNavController(navController)

<<<<<<< HEAD:app/src/main/java/com/example/mon_project/MainActivity.kt
=======
        // Добавляем действия по нажатию к кнопкам меню
>>>>>>> bd20ec8 (Bug fixes. SearchPage was implemented):app/src/main/java/com/example/montee_project/MainActivity.kt
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.main_page_button -> {
                    Log.d(TAG, supportFragmentManager.backStackEntryCount.toString())
                    if (supportFragmentManager.fragments.last() !is MainPage) {
                        openFragment(MainPage.newInstance("", ""))
                        toolbar.title = getString(R.string.main_page_label)
                    }
                    true
                }
                R.id.search_page_button -> {
                    if (supportFragmentManager.fragments.last() !is SearchPage) {
                        openFragment(SearchPage.newInstance("", ""))
                        toolbar.title = getString(R.string.search_page_label)
                    }
                    true
                }
                R.id.add_food_page_button -> {
                    if (supportFragmentManager.fragments.last() !is AddMealPage) {
                        openFragment(AddMealPage.newInstance("", ""))
                        toolbar.title = getString(R.string.add_meal_page_label)
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
                        openFragment(ShoppingListPage.newInstance("", ""))
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

<<<<<<< HEAD:app/src/main/java/com/example/mon_project/MainActivity.kt
=======
    // Функция перехода во фрагмент и анимации
>>>>>>> bd20ec8 (Bug fixes. SearchPage was implemented):app/src/main/java/com/example/montee_project/MainActivity.kt
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