package com.example.healthyeatingapp

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), Fragment_QRcode.OnFragmentInteractionListener,
    Fragment_QRCodeConfirmation.OnFragmentInteractionListener {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var dbHelper_food: DBHelper_Food

    private lateinit var food: DataRecord_Food

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            val fragment = Fragment_Dashboard()
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_fragmentLayout, fragment, fragment.javaClass.getSimpleName())
                .commit()
        }

        bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)

        bottomNavigationView.setSelectedItemId(R.id.navigation_dashboard)
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        dbHelper_food = DBHelper_Food(this)

    }

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_dashboard -> {
                    val fragment = Fragment_Dashboard()
                    supportFragmentManager.beginTransaction().replace(
                        R.id.main_fragmentLayout,
                        fragment,
                        fragment.javaClass.getSimpleName()
                    )
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_wallet -> {
                    val fragment = Fragment_Wallet()
                    supportFragmentManager.beginTransaction().replace(
                        R.id.main_fragmentLayout,
                        fragment,
                        fragment.javaClass.getSimpleName()
                    )
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_qrcode -> {
                    val fragment = Fragment_QRcode()
                    supportFragmentManager.beginTransaction().replace(
                        R.id.main_fragmentLayout,
                        fragment,
                        fragment.javaClass.getSimpleName()
                    )
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_map -> {
                    val fragment = Fragment_Maps()
                    supportFragmentManager.beginTransaction().replace(
                        R.id.main_fragmentLayout,
                        fragment,
                        fragment.javaClass.getSimpleName()
                    )
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_profile -> {
                    val fragment = Fragment_Profile()
                    supportFragmentManager.beginTransaction().replace(
                        R.id.main_fragmentLayout,
                        fragment,
                        fragment.javaClass.getSimpleName()
                    )
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    override fun onFragmentInteractionQRCode(food: DataRecord_Food?) {
        if (food != null) {
            this.food = food
            val fragment = Fragment_QRCodeConfirmation.newInstance(food)
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_fragmentLayout, fragment, fragment.javaClass.getSimpleName())
                .commit()
        }
    }

    override fun onFragmentInteractionQRCodeConfirmation(result: Boolean) {
        if (result) {
            dbHelper_food.insertFood(food)

//            dbHelper_food.clearDatabase()
            var foods = dbHelper_food.readAllFoods()
            var str: String = ""
            for (food in foods)
                str += food.name + "\n"

            Toast.makeText(this, "SUCCESS!\n" + str, Toast.LENGTH_LONG).show()
            bottomNavigationView.setSelectedItemId(R.id.navigation_dashboard)
        }
    }

}
