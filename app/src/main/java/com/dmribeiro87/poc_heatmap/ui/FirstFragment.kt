package com.dmribeiro87.poc_heatmap.ui

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.findNavController
import com.dmribeiro87.poc_heatmap.MainActivity
import com.dmribeiro87.poc_heatmap.R
import com.dmribeiro87.poc_heatmap.databinding.FragmentFirstBinding
import com.dmribeiro87.poc_heatmap.model.Boundary
import com.dmribeiro87.poc_heatmap.model.HexagonData
import com.dmribeiro87.poc_heatmap.ui.viewmodel.HeatMapViewModel
import com.dmribeiro87.poc_heatmap.utils.PolygonColors
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnCameraIdleListener
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polygon
import com.google.android.gms.maps.model.PolygonOptions
import org.koin.androidx.viewmodel.ext.android.viewModel


class FirstFragment : Fragment(), OnCameraIdleListener {

    private var _binding: FragmentFirstBinding? = null
    private lateinit var googleMap: GoogleMap
    private lateinit var currentLocation: Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val polygons = mutableListOf<Polygon>()
    private val polygonColors = PolygonColors()
    private val permissionCode = 1001

    private val binding get() = _binding!!
    private val viewModel: HeatMapViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        viewModel.getHexagons()
        getCurrentLocationUser()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            permissionCode -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getCurrentLocationUser()
            }
        }
    }

    override fun onCameraIdle() {
        if(::googleMap.isInitialized) {
            val visibleRegion = googleMap.projection.visibleRegion

            // Cria um objeto LatLngBounds com base na área visível
            val bounds = LatLngBounds.builder()
                .include(visibleRegion.farLeft)
                .include(visibleRegion.farRight)
                .include(visibleRegion.nearLeft)
                .include(visibleRegion.nearRight)
                .build()

            for (polygon in polygons) {
                val polygonBounds = LatLngBounds.builder()
                for (point in polygon.points) {
                    polygonBounds.include(point)
                }
                // Mostra apenas os 7 pontos do exágono que estiverem na área vissível
                polygon.isVisible =
                    bounds.contains(polygonBounds.build().northeast) && bounds.contains(
                        polygonBounds.build().southwest
                    )
            }
        }
    }


    private fun getCurrentLocationUser() {
        if (ActivityCompat.checkSelfPermission(
            requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), permissionCode)
            return
        }
        val getLocationUser = fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->

            if (location != null){
                currentLocation = location
                Toast.makeText(requireContext(), currentLocation.latitude.toString() + ", "  + currentLocation.longitude.toString(), Toast.LENGTH_LONG).show()
                val mapFragment = childFragmentManager.findFragmentById(R.id.heat_map) as SupportMapFragment
                mapFragment.getMapAsync { map ->

                        setMap(map)
                        val latLng = LatLng(currentLocation.latitude, currentLocation.longitude)
                        val markerOptions = MarkerOptions().position(latLng).title("Current Location")
                        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                        googleMap.addMarker(markerOptions)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setMap(googleMap: GoogleMap) {
        this.googleMap = googleMap
        this.googleMap.uiSettings.apply { isZoomControlsEnabled = true }
        this.googleMap.setOnCameraIdleListener(this)

        viewModel.hexagonData.value?.let {
            updateHexagons(it)
        }
    }

    private fun updateHexagons(list: List<HexagonData>) {
        list.forEach { data ->
            val polygonOptions = PolygonOptions()
            polygonOptions.fillColor(polygonColors.scoreToFillColor(data.score))
            polygonOptions.strokeWidth(1F)
            polygonOptions.strokeColor(polygonColors.scoreToStrokeColor(data.score))
            val boundary = convertBoundaryToCoordinates(data.boundary)
            boundary.forEach { hexagons ->
                polygonOptions.addAll(hexagons)
                Log.d("***polygonOptions", polygonOptions.points.size.toString())
            }
            Log.d("***boundary", boundary.toString())

            if (boundary.isNotEmpty()) {
                if(::googleMap.isInitialized) {
                    polygons.add(googleMap.addPolygon(polygonOptions))
                }
            }
        }
    }

    private fun invertedLatLng(list: List<List<List<Double>>>?): List<List<LatLng>> {
        return list!!.map { outerList ->
            outerList.map { innerList ->
                LatLng(innerList[1], innerList[0])
            }
        }
    }

    private fun convertBoundaryToCoordinates(boundaryDto: Boundary?) : List<List<LatLng>> {
        val data = boundaryDto?.coordinates
        return invertedLatLng(data)
    }
}