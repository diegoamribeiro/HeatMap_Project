package com.dmribeiro87.poc_heatmap.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dmribeiro87.poc_heatmap.Repository
import com.dmribeiro87.poc_heatmap.model.HexagonData
import com.dmribeiro87.poc_heatmap.model.Resource
import kotlinx.coroutines.launch

class HeatMapViewModel(
    private val repository: Repository
): ViewModel() {

    private val _hexagonData = MutableLiveData<List<HexagonData>>()
    val hexagonData: LiveData<List<HexagonData>> = _hexagonData

    fun getHexagons(){
        viewModelScope.launch {
            val resource = repository.getHexagons()

            _hexagonData.value = resource.data!!
        }
    }

}