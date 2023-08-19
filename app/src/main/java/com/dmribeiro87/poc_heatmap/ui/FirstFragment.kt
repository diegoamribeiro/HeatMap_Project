package com.dmribeiro87.poc_heatmap.ui

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.dmribeiro87.poc_heatmap.R
import com.dmribeiro87.poc_heatmap.databinding.FragmentFirstBinding
import com.dmribeiro87.poc_heatmap.ui.viewmodel.HeatMapViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import org.koin.androidx.viewmodel.ext.android.viewModel


class FirstFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentFirstBinding? = null
    private var googleMap: GoogleMap? = null

    private val binding get() = _binding!!
    private val viewModel: HeatMapViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        val mapFragment = childFragmentManager.findFragmentById(R.id.heat_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getHexagons()
        observeData()
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

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
    }
}