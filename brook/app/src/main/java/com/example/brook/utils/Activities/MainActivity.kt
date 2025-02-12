package com.example.brook.utils.Activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.onNavDestinationSelected
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.brook.R
import com.example.brook.model.Model.Companion.instance
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            getSupportFragmentManager().findFragmentById(R.id.main_navhost) as NavHostFragment?
        navController = navHostFragment!!.navController
        setupActionBarWithNavController(this, navController!!)

        val navView = findViewById<BottomNavigationView>(R.id.main_bottomNavigationView)
        setupWithNavController(navView, navController!!)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> navController!!.popBackStack()
            R.id.logout -> {
                instance().logout()
                val navToActivityIntent = Intent(this, LogInActivity::class.java)
                startActivity(navToActivityIntent)
            }

            else -> return onNavDestinationSelected(item, navController!!)
        }

        return super.onOptionsItemSelected(item)
    }
}