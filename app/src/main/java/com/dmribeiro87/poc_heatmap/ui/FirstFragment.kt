package com.dmribeiro87.poc_heatmap.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.dmribeiro87.poc_heatmap.R
import com.dmribeiro87.poc_heatmap.databinding.FragmentFirstBinding
import com.dmribeiro87.poc_heatmap.ui.viewmodel.HeatMapViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

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

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
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
}