package com.dmribeiro87.poc_heatmap

import android.content.Context
import com.dmribeiro87.poc_heatmap.model.HexagonResponse
import com.google.gson.Gson
import java.nio.charset.Charset

class Repository(private val context: Context) {

    fun getHexagons(): HexagonResponse {
        val gson = Gson()
        val jsonString = readJSONFromAsset()
        return gson.fromJson(jsonString, HexagonResponse::class.java)
    }

    private fun readJSONFromAsset(): String {
        val inputStream = context.assets.open("location_sp.json")
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        return String(buffer, Charset.forName("UTF-8"))
    }
}

