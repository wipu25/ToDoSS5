package com.example.todoss5.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.todoss5.R
import com.example.todoss5.SharedPrefUtil
import com.example.todoss5.arch.ToBuyViewModel
import com.example.todoss5.database.AppDatabase
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity:AppCompatActivity() {

    lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        SharedPrefUtil.init(this)

        val viewModel: ToBuyViewModel by viewModels()
        viewModel.init(AppDatabase.getDatabase(this))

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController

        //set app bar name accr page
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.homeFragment,
            R.id.profileFragment
        ))
        setupActionBarWithNavController(navController,appBarConfiguration)

        //setup bottom nav bar
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavBar)
        setupWithNavController(bottomNavigationView, navController)

        //add destination change listener to show/hide the bottom nav bar
        navController.addOnDestinationChangedListener{ controller,destination, argumnets ->
            //top level destination - home, profile / others - hide nav bar
            if (appBarConfiguration.topLevelDestinations.contains(destination.id)) {
                bottomNavigationView.isVisible = true
            } else {
                bottomNavigationView.isGone = true
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun hideKeyboard(view: View) {
        val inputMethodManage: InputMethodManager = application.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManage.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun showKeyboard() {
        val inputMethodManage: InputMethodManager = application.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManage.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }
}