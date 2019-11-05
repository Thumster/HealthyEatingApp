package com.example.healthyeatingapp

import android.Manifest
import android.annotation.TargetApi
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), Fragment_QRcode.OnFragmentInteractionListener,
    Fragment_QRCodeConfirmation.OnFragmentInteractionListener {

    var allPermissionsGrantedFlag: Int = 0
    private val permissionList = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var dbHelper_food: DBHelper_Food

    private lateinit var food: DataRecord_Food

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (allPermissionsEnabled()) {
                allPermissionsGrantedFlag = 1
            } else {
                setupMultiplePermissions()
                allPermissionsEnabled()
            }
        } else {
            allPermissionsGrantedFlag = 1
            Log.e("VALUE", "1")
        }

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
                    if (allPermissionsGrantedFlag == 1) {
                        val fragment = Fragment_QRcode()
                        supportFragmentManager.beginTransaction().replace(
                            R.id.main_fragmentLayout,
                            fragment,
                            fragment.javaClass.getSimpleName()
                        )
                            .commit()
                        return@OnNavigationItemSelectedListener true
                    } else {
                        setupMultiplePermissions()
                    }
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

    @RequiresApi(Build.VERSION_CODES.M)
    private fun allPermissionsEnabled(): Boolean {
        var result: Boolean = permissionList.none {
            checkSelfPermission(it) !=
                    PackageManager.PERMISSION_GRANTED
        }
        if (result) {
            allPermissionsGrantedFlag = 1
        }
        return result
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setupMultiplePermissions() {
        val remainingPermissions = permissionList.filter {
            checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED
        }
        if (!remainingPermissions.isEmpty()) {
            requestPermissions(remainingPermissions.toTypedArray(), 101)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissionList: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissionList, grantResults)
        if (requestCode == 101) {
            if (grantResults.any { it != PackageManager.PERMISSION_GRANTED }) {
                @TargetApi(Build.VERSION_CODES.M)
                if (permissionList.any { shouldShowRequestPermissionRationale(it) }) {
                    AlertDialog.Builder(this)
                        .setMessage("Press Permissions to Decide Permission Again")
                        .setPositiveButton("Permissions") { dialog, which -> setupMultiplePermissions() }
                        .setNegativeButton("Cancel") { dialog, which -> dialog.dismiss() }
                        .create()
                        .show()
                }
            }
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                allPermissionsGrantedFlag = 1
            }
        }
    }
}
