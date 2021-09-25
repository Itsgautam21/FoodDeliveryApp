package com.example.loginApp

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView

class MainActivity23 : AppCompatActivity() {

    private var mAppBarConfiguration: AppBarConfiguration? = null
    var view: View? = null
    var sessionManager: SessionManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main23)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(Color.BLACK)
        sessionManager = SessionManager(this@MainActivity23)
        val user = sessionManager!!.userDetailFromSession
        val fullName = user[SessionManager.KEY_FULL_NAME]
        val email = user[SessionManager.KEY_PHONE_NO]
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        val view = navigationView.getHeaderView(0)
        val name = view.findViewById<TextView>(R.id.nameViewHeader)
        val emailId = view.findViewById<TextView>(R.id.emailViewHeader)
        name.text = fullName
        emailId.text = email
        toolbar.setNavigationIcon(R.drawable.menu)
        mAppBarConfiguration = AppBarConfiguration.Builder(R.id.nav_home)
            .setDrawerLayout(drawer)
            .build()
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration!!)
        NavigationUI.setupWithNavController(navigationView, navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_activity23, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        return NavigationUI.navigateUp(navController, mAppBarConfiguration!!) || super.onSupportNavigateUp()
    }

}