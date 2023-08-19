package com.dmribeiro87.poc_heatmap.model

sealed class Resource<out T : Any>(
    val data: Any? = null,
    val message: String? = null
) : ViewState(){
    class Success<out T : Any>(data: T): Resource<T>(data)
    class Error<out T : Any>(message: T? = null): Resource<Nothing>(message)
    object Loading: Resource<Nothing>()
}