package com.example.healthyeatingapp

import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var lastLocation: Location? = null
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private var locationUpdateState = false

    val foodclique = place(
        "Foodclique",
        "PGPR",
        "318",
        "13",
        "Mon-Sun, 7.00am-9.30pm",
        LatLng(1.290940, 103.782217),
        R.drawable.map_place_foodclique
    )
    val frontier = place(
        "Frontier",
        "Faculty of Science",
        "700",
        "15",
        "Mon-Fri, 7.30am-4.00pm/8.00pm\nSat, 7.30-am-3.00pm\nSun/PH closed",
        LatLng(1.296748, 103.780561),
        R.drawable.map_place_frontier
    )
    val thedeck = place(
        "The Deck",
        "Faculty of Arts & Social Sciences",
        "1018",
        "13",
        "Mon-Fri, 7.30am-4.00pm/8.00pm\nSat,7.30am-3.00pm\nSun/PH closed",
        LatLng(1.294728, 103.772458),
        R.drawable.map_place_thedeck
    )
    val finefood = place(
        "Fine Food",
        "Town Plaza",
        "400",
        "14",
        "Mon-Sun, 7.00am-10.00pm",
        LatLng(1.305404, 103.772715),
        R.drawable.map_place_finefood
    )
    val platypusFoodBar = place(
        "Platypus Food Bar",
        "Engineering Block E2A",
        "-",
        "-",
        "Mon-Fri, 9.00am-7.30pm",
        LatLng(1.298795, 103.771560),
        R.drawable.map_place_platypus
    )
    val centralSquare = place(
        "Central Square",
        "Yusof Ishak House Level 2",
        "363",
        "11",
        "Mon-Fri, 8.00am-8.00pm\nSat, 8.00am-3.00pm\nSun/PH closed",
        LatLng(1.298520, 103.775052),
        R.drawable.map_place_centralsquare
    )

    val places = mapOf(
        "Foodclique" to foodclique,
        "Frontier" to frontier,
        "The Deck" to thedeck,
        "Fine Food" to finefood,
        "Platypus Food Bar" to platypusFoodBar,
        "Central Square" to centralSquare
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult?) {
                super.onLocationResult(p0)
                lastLocation = p0?.lastLocation
                if (lastLocation != null) {

                }
            }
        }

        createLocationRequest()

    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val singapore = LatLng(1.2966, 103.7764)

        places.forEach {
            mMap.addMarker(
                MarkerOptions().position(it.value.latLng).title(it.value.l_name).snippet(
                    it.value.l_location
                ).icon(BitmapDescriptorFactory.defaultMarker())
            )
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLng(singapore))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(singapore, 15.0f))

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isZoomGesturesEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL

        var lastOpened: Marker? = null

        mMap.setOnInfoWindowClickListener {

            var place = places.get(it.title);


            val informationView = LayoutInflater.from(this)
                .inflate(
                    R.layout.map_information,
                    null,
                    false
                ) as LinearLayout

            informationView.findViewById<ImageView>(R.id.map_image)
                .setImageResource(place?.l_image!!)
            informationView.findViewById<TextView>(R.id.map_location_name).text = place?.l_name
            informationView.findViewById<TextView>(R.id.map_location).text = place?.l_location
            informationView.findViewById<TextView>(R.id.map_capacity).text = place?.l_capacity
            informationView.findViewById<TextView>(R.id.map_stalls).text = place?.l_stalls
            informationView.findViewById<TextView>(R.id.map_opening_hours).text =
                place?.l_openinghours

            MaterialAlertDialogBuilder(
                this,
                R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog
            )
                .setView(informationView)
                .setNegativeButton("Dismiss") { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }

        mMap.setOnMarkerClickListener(OnMarkerClickListener { marker ->
            if (lastOpened != null) {
                lastOpened!!.hideInfoWindow()

                if (lastOpened!!.equals(marker)) {
                    lastOpened = null
                    return@OnMarkerClickListener true
                }
            }

            marker.showInfoWindow()
            lastOpened = marker

            true
        })

        setUpMap()

    }

    fun getMarkerIcon(color: String): BitmapDescriptor {
        val hsv = FloatArray(3)
        Color.colorToHSV(Color.parseColor(color), hsv)
        return BitmapDescriptorFactory.defaultMarker(hsv[0])
    }

    private fun setUpMap() {
        mMap?.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)

                mMap?.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        currentLatLng,
                        17.0f
                    )
                )
            }
        }
    }

    private fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback, null
        )
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 5000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val client = LocationServices.getSettingsClient(this)

        val task = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            locationUpdateState = true
            startLocationUpdates()
        }
    }

    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    public override fun onResume() {
        super.onResume()
        if (!locationUpdateState) {
            startLocationUpdates()
        }
    }

}


class place(
    var l_name: String,
    var l_location: String,
    var l_capacity: String,
    var l_stalls: String,
    var l_openinghours: String,
    var latLng: LatLng,
    var l_image: Int
) {

}