package com.example.healthyeatingapp

import android.Manifest
import android.annotation.TargetApi
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.healthyeatingapp.Food.DBHelper_Food
import com.example.healthyeatingapp.Food.DataRecord_Food
import com.example.healthyeatingapp.Points.DBHelper_Points
import com.example.healthyeatingapp.Profile.DBHelper_Profile
import com.example.healthyeatingapp.Wallet.DBHelper_Transaction
import com.example.healthyeatingapp.Wallet.DataRecord_Transaction
import com.example.healthyeatingapp.enumeration.TransactionType
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), Fragment_QRcode.OnFragmentInteractionListener,
    Fragment_QRCodeConfirmation.OnFragmentInteractionListener,
    Fragment_Profile.OnFragmentInteractionListener,
    Fragment_Dashboard.OnFragmentInteractionListener {

    private val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")

    var allPermissionsGrantedFlag: Int = 0
    var cameraPermissionGrantedFlag: Int = 0
    var locationPermissionGrantedFlag: Int = 0
    private val permissionList = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    private lateinit var bottomNavigationView: BottomNavigationView

    private lateinit var dbHelper_food: DBHelper_Food
    private lateinit var dbHelper_transaction: DBHelper_Transaction
    private lateinit var dbHelper_profile: DBHelper_Profile
    private lateinit var dbHelper_points: DBHelper_Points

    private lateinit var food: DataRecord_Food

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setIcon(R.mipmap.ic_launcher_round)
        supportActionBar?.setBackgroundDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.ab_gradient
            )
        )

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
        dbHelper_transaction = DBHelper_Transaction(this)
        dbHelper_profile = DBHelper_Profile(this)
        dbHelper_points = DBHelper_Points(this)

        dbHelper_profile.getProfile()
        dbHelper_transaction.readAllTransactions()
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
                    if (cameraPermissionGrantedFlag == 1) {
                        val fragment = Fragment_QRcode()
                        supportFragmentManager.beginTransaction().replace(
                            R.id.main_fragmentLayout,
                            fragment,
                            fragment.javaClass.getSimpleName()
                        )
                            .commit()
                        return@OnNavigationItemSelectedListener true
                    } else {
                        setupCameraPermission()
                    }
                }
                R.id.navigation_map -> {
                    if (locationPermissionGrantedFlag == 1) {

                        val myIntent = Intent(this, MapsActivity::class.java)
                        startActivity(myIntent)

//                        return@OnNavigationItemSelectedListener true
                    } else {
                        setupLocationPermission()
                    }
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
            val balance = DBHelper_Transaction.balance
            val foodPrice = food.price
            val foodName = food.name
            var strResult = ""

            if (balance >= foodPrice) {
                dbHelper_food.insertFood(food)

                val date_and_time = sdf.format(Date())
                dbHelper_transaction.insertTransaction(
                    DataRecord_Transaction(
                        TransactionType.CREDIT,
                        foodName,
                        foodPrice,
                        date_and_time
                    )
                )
                strResult = "Success! Purchased " + foodName + " for S$ " + String.format(
                    "%.2f",
                    foodPrice
                )
                bottomNavigationView.setSelectedItemId(R.id.navigation_dashboard)
                val fragment = Fragment_Dashboard.newInstance(food)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_fragmentLayout, fragment, fragment.javaClass.getSimpleName())
                    .commit()

            } else {
                strResult = "INSUFFICIENT BALANCE:\nPlease Top Up your wallet!"
                bottomNavigationView.setSelectedItemId(R.id.navigation_wallet)
            }
            val toast = Toast.makeText(
                this,
                strResult,
                Toast.LENGTH_LONG
            )
            val view = toast.view.findViewById<TextView>(android.R.id.message)
            view?.let {
                view.gravity = Gravity.CENTER
            }
            toast.show()
        }
    }

    override fun onFragmentInteractionProfileDeleteDatabase(boo: Boolean) {
        if (boo) {
            dbHelper_transaction.clearDatabase()
            dbHelper_food.clearDatabase()
            dbHelper_profile.clearDatabase()
            dbHelper_points.clearDatabase()
            val toast = Toast.makeText(
                this,
                "SUCCESSFULLY Reset!",
                Toast.LENGTH_LONG
            )
            val view = toast.view.findViewById<TextView>(android.R.id.message)
            view?.let {
                view.gravity = Gravity.CENTER
            }
            toast.show()
        }
    }

    override fun onFragmentInteractionNavigateToProfile() {
        bottomNavigationView.setSelectedItemId(R.id.navigation_profile)
    }

    //PERMISSIONS   PERMISSIONS   PERMISSIONS   PERMISSIONS   PERMISSIONS   PERMISSIONS   PERMISSIONS
    //PERMISSIONS   PERMISSIONS   PERMISSIONS   PERMISSIONS   PERMISSIONS   PERMISSIONS   PERMISSIONS
    //PERMISSIONS   PERMISSIONS   PERMISSIONS   PERMISSIONS   PERMISSIONS   PERMISSIONS   PERMISSIONS
    //PERMISSIONS   PERMISSIONS   PERMISSIONS   PERMISSIONS   PERMISSIONS   PERMISSIONS   PERMISSIONS
    //PERMISSIONS   PERMISSIONS   PERMISSIONS   PERMISSIONS   PERMISSIONS   PERMISSIONS   PERMISSIONS
    //PERMISSIONS   PERMISSIONS   PERMISSIONS   PERMISSIONS   PERMISSIONS   PERMISSIONS   PERMISSIONS


    @RequiresApi(Build.VERSION_CODES.M)
    private fun allPermissionsEnabled(): Boolean {
        var result: Boolean = permissionList.none {
            checkSelfPermission(it) !=
                    PackageManager.PERMISSION_GRANTED
        }
        if (result) {
            allPermissionsGrantedFlag = 1
            cameraPermissionGrantedFlag = 1
            locationPermissionGrantedFlag = 1
        }
        return result
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setupMultiplePermissions() {
        val remainingPermissions = permissionList.filter {
            checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED
        }
        requestPermissions(remainingPermissions.toTypedArray(), 101)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setupCameraPermission() {
        val remainingPermissions = permissionList.filter {
            it == Manifest.permission.CAMERA
        }
        requestPermissions(remainingPermissions.toTypedArray(), 102)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setupLocationPermission() {
        val remainingPermissions = permissionList.filter {
            it == Manifest.permission.ACCESS_FINE_LOCATION
        }
        requestPermissions(remainingPermissions.toTypedArray(), 103)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissionList: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissionList, grantResults)
        // FOR MULTIPLE PERMISSIONS
        if (requestCode == 101) {
            if (grantResults.any { it != PackageManager.PERMISSION_GRANTED }) {
                @TargetApi(Build.VERSION_CODES.M)
                if (permissionList.any { shouldShowRequestPermissionRationale(it) }) {
                    MaterialAlertDialogBuilder(
                        this,
                        R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog
                    )
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

        // FOR CAMERA PERMISSION
        if (requestCode == 102) {
            if (grantResults.any { it != PackageManager.PERMISSION_GRANTED }) {
                @TargetApi(Build.VERSION_CODES.M)
                if (permissionList.any { shouldShowRequestPermissionRationale(it) }) {
                    MaterialAlertDialogBuilder(
                        this,
                        R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog
                    )
                        .setMessage("Press Permissions to Decide Permission Again")
                        .setPositiveButton("Permissions") { dialog, which -> setupCameraPermission() }
                        .setNegativeButton("Cancel") { dialog, which -> dialog.dismiss() }
                        .create()
                        .show()
                }
            }
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                cameraPermissionGrantedFlag = 1
                bottomNavigationView.setSelectedItemId(R.id.navigation_qrcode)
            }
        }

        // FOR LOCATION PERMISSION
        if (requestCode == 103) {
            if (grantResults.any { it != PackageManager.PERMISSION_GRANTED }) {
                @TargetApi(Build.VERSION_CODES.M)
                if (permissionList.any { shouldShowRequestPermissionRationale(it) }) {
                    MaterialAlertDialogBuilder(
                        this,
                        R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog
                    )
                        .setMessage("Press Permissions to Decide Permission Again")
                        .setPositiveButton("Permissions") { dialog, which -> setupLocationPermission() }
                        .setNegativeButton("Cancel") { dialog, which -> dialog.dismiss() }
                        .create()
                        .show()
                }
            }
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                locationPermissionGrantedFlag = 1
                bottomNavigationView.setSelectedItemId(R.id.navigation_map)
            }

        }
    }
}
