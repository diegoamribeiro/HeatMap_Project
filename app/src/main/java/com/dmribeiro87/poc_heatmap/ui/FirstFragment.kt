package com.dmribeiro87.poc_heatmap.ui

import android.Manifest
import android.content.pm.PackageManager
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
import com.dmribeiro87.poc_heatmap.R
import com.dmribeiro87.poc_heatmap.databinding.FragmentFirstBinding
import com.dmribeiro87.poc_heatmap.ui.viewmodel.HeatMapViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnCameraIdleListener
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import org.koin.androidx.viewmodel.ext.android.viewModel


class FirstFragment : Fragment(), OnCameraIdleListener {

    private var _binding: FragmentFirstBinding? = null
    private var googleMap: GoogleMap? = null
    private lateinit var currentLocation: Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
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
        observeData()
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
                    googleMap = map
                    val latLng = LatLng(currentLocation.latitude, currentLocation.longitude)
                    val markerOptions = MarkerOptions().position(latLng).title("Current Location")
                    googleMap?.animateCamera(CameraUpdateFactory.newLatLng(latLng))
                    googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18f))
                    googleMap?.addMarker(markerOptions)
                }
            }
        }
    }

    private fun observeData() {
        viewModel.hexagonData.observe(viewLifecycleOwner){
            Log.d("***Data", it.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}