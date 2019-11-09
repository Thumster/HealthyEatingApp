package com.example.healthyeatingapp

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import android.graphics.Color.parseColor
import android.graphics.Color.colorToHSV
import android.graphics.drawable.Drawable
import com.google.android.gms.maps.model.BitmapDescriptor
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.util.Log
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.media.Image
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.function.BiConsumer


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    val foodclique = place("Foodclique","PGPR", "318", "13","Mon-Sun, 7.00am-9.30pm", LatLng(1.290940, 103.782217), R.drawable.map_place_foodclique)
    val frontier = place("Frontier","Faculty of Science", "700", "15","Mon-Fri, 7.30am-4.00pm/8.00pm\nSat, 7.30-am-3.00pm\nSun/PH closed", LatLng(1.296748, 103.780561), R.drawable.map_place_frontier)
    val thedeck = place("The Deck","Faculty of Arts & Social Sciences", "1018", "13","Mon-Fri, 7.30am-4.00pm/8.00pm\nSat,7.30am-3.00pm\nSun/PH closed", LatLng(1.294728, 103.772458), R.drawable.map_place_thedeck)
    val finefood = place("Fine Food","Town Plaza", "400", "14","Mon-Sun, 7.00am-10.00pm", LatLng(1.305404, 103.772715), R.drawable.map_place_finefood)
    val platypusFoodBar = place("Platypus Food Bar","Engineering Block E2A", "-", "-","Mon-Fri, 9.00am-7.30pm", LatLng(1.298795, 103.771560), R.drawable.map_place_platypus)
    val centralSquare = place("Central Square","Yusof Ishak House Level 2", "363", "11","Mon-Fri, 8.00am-8.00pm\nSat, 8.00am-3.00pm\nSun/PH closed", LatLng(1.298520, 103.775052), R.drawable.map_place_centralsquare)

    val places = mapOf("Foodclique" to foodclique, "Frontier" to frontier, "The Deck" to thedeck, "Fine Food" to finefood, "Platypus Food Bar" to platypusFoodBar, "Central Square" to centralSquare)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val singapore = LatLng(1.2966, 103.7764)





        places.forEach{
            //mMap.addMarker(MarkerOptions().position(it.value.latLng).title(it.value.l_name).snippet(it.value.l_location).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_pgp_80_80)))
            mMap.addMarker(MarkerOptions().position(it.value.latLng).title(it.value.l_name).snippet(it.value.l_location).icon(BitmapDescriptorFactory.defaultMarker()))
        }

//        mMap.addMarker(MarkerOptions().position(foodclique).title("Food Clique").snippet("Prince Gorge Park Residences").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_pgp_80_80)))
//        mMap.addMarker(MarkerOptions().position(frontier).title("Frontier").snippet("Faculty of Science").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_pgp_80_80)))
//        mMap.addMarker(MarkerOptions().position(thedeck).title("The Deck").snippet("Arts").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_pgp_80_80)))
//        mMap.addMarker(MarkerOptions().position(finefood).title("UTown - Fine Food").snippet("U-Town").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_pgp_80_80)))
//        mMap.addMarker(MarkerOptions().position(platypusFoodBar).title("Engineering - Platypus").snippet("Faculty of Engineering").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_pgp_80_80)))
//        mMap.addMarker(MarkerOptions().position(centralSquare).title("YIH - Food Junction").snippet("Yusof Ishak House").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_pgp_80_80)))


        mMap.moveCamera(CameraUpdateFactory.newLatLng(singapore))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(singapore, 15.0f))

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isZoomGesturesEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        var lastOpened: Marker? = null

        mMap.setOnInfoWindowClickListener{

            var place = places.get(it.title);


            val informationView = LayoutInflater.from(this)
                .inflate(
                    R.layout.map_information,
                    null,
                    false
                ) as LinearLayout

            informationView.findViewById<ImageView>(R.id.map_image).setImageResource(place?.l_image!!)
            informationView.findViewById<TextView>(R.id.map_location_name).text = place?.l_name
            informationView.findViewById<TextView>(R.id.map_location).text = place?.l_location
            informationView.findViewById<TextView>(R.id.map_capacity).text = place?.l_capacity
            informationView.findViewById<TextView>(R.id.map_stalls).text = place?.l_stalls
            informationView.findViewById<TextView>(R.id.map_opening_hours).text = place?.l_openinghours

            MaterialAlertDialogBuilder(
                this,
                R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog
            )
                .setView(informationView)
                .setNegativeButton("Dismiss") { dialog, which -> dialog.dismiss() }
                .create()
                .show()

//            Log.e("INFO WINDOW", infowindow.getTitle())



        }


        mMap.setOnMarkerClickListener(OnMarkerClickListener { marker ->
            // Check if there is an open info window
            if (lastOpened != null) {
                // Close the info window
                lastOpened!!.hideInfoWindow()

                // Is the marker the same marker that was already open
                if (lastOpened!!.equals(marker)) {
                    // Nullify the lastOpened object
                    lastOpened = null
                    // Return so that the info window isn't opened again
                    return@OnMarkerClickListener true
                }
            }

            Log.e("Marker", "Mokie")
            // Open the info window for the marker
            marker.showInfoWindow()
            // Re-assign the last opened such that we can close it later
            lastOpened = marker

            // Event was handled by our code do not launch default behaviour.
            true
        })

    }

    fun getMarkerIcon(color: String): BitmapDescriptor {
        val hsv = FloatArray(3)
        Color.colorToHSV(Color.parseColor(color), hsv)
        return BitmapDescriptorFactory.defaultMarker(hsv[0])
    }
}


class place( var l_name:String,  var l_location: String,  var l_capacity: String, var l_stalls: String, var l_openinghours: String, var latLng: LatLng, var l_image : Int) {

}