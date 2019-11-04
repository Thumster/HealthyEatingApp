package com.example.healthyeatingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            val fragment = Fragment_Dashboard()
            supportFragmentManager.beginTransaction().replace(R.id.main_fragmentLayout, fragment, fragment.javaClass.getSimpleName())
                .commit()
        }

        val bottomNavigationView =  findViewById<BottomNavigationView>(R.id.bottom_navigation_view)

        bottomNavigationView.setSelectedItemId(R.id.navigation_dashboard)
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
        when (menuItem.itemId) {
            R.id.navigation_dashboard -> {
                val fragment = Fragment_Dashboard()
                supportFragmentManager.beginTransaction().replace(R.id.main_fragmentLayout, fragment, fragment.javaClass.getSimpleName())
                    .commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_wallet -> {
                val fragment = Fragment_Wallet()
                supportFragmentManager.beginTransaction().replace(R.id.main_fragmentLayout, fragment, fragment.javaClass.getSimpleName())
                    .commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_qrcode -> {
                val fragment = Fragment_QRcode()
                supportFragmentManager.beginTransaction().replace(R.id.main_fragmentLayout, fragment, fragment.javaClass.getSimpleName())
                    .commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_map -> {
                val fragment = Fragment_Maps()
                supportFragmentManager.beginTransaction().replace(R.id.main_fragmentLayout, fragment, fragment.javaClass.getSimpleName())
                    .commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> {
                val fragment = Fragment_Profile()
                supportFragmentManager.beginTransaction().replace(R.id.main_fragmentLayout, fragment, fragment.javaClass.getSimpleName())
                    .commit()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

}
