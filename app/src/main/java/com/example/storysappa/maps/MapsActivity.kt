package com.example.storysappa.maps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.storysappa.R
import com.example.storysappa.ViewModelFactory

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.storysappa.databinding.ActivityMapsBinding

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mapsViewModel: MapsViewModel
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelFactory.getInstance(this)
        mapsViewModel = ViewModelProvider(this, factory).get(MapsViewModel::class.java)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mapsViewModel.getStoriesWithLocation()

        mapsViewModel.storiesWithLocation.observe(this) { response ->
            response.listStory?.forEach { listStory ->
                if (listStory != null) {
                    if (listStory.lat != null && listStory.lon != null) {
                        val latLng = LatLng(listStory.lat, listStory.lon)
                        mMap.addMarker(
                            MarkerOptions()
                                .position(latLng)
                                .title(listStory.name)
                                .snippet(listStory.description)
                        )
                    } else {
                        Log.e("MapsActivity", "Story ${listStory.name} does not have valid location.")
                    }
                }
            }
        }

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

    }
}