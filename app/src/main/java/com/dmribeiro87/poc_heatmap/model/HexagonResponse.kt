package com.dmribeiro87.poc_heatmap.model


import com.google.gson.annotations.SerializedName

data class HexagonResponse(
    @SerializedName("data")
    val data: List<HexagonData>?,
    @SerializedName("pagination")
    val pagination: Pagination?
)

data class HexagonColor(
    @SerializedName("fill_color")
    val fillColor: String,
    @SerializedName("fill_opacity")
    val fillOpacity: Double,
    @SerializedName("range_end")
    val rangeEnd: Int,
    @SerializedName("range_start")
    val rangeStart: Int,
    @SerializedName("stroke_color")
    val strokeColor: String,
    @SerializedName("stroke_opacity")
    val strokeOpacity: Double,
    @SerializedName("stroke_weight")
    val strokeWeight: Int
)

data class HexagonData(
    @SerializedName("boundary")
    val boundary: Boundary,
    @SerializedName("couriers")
    val couriers: Int,
    @SerializedName("couriers_ids")
    val couriersIds: List<String>,
    @SerializedName("deliveries")
    val deliveries: Int,
    @SerializedName("deliveries_ids")
    val deliveriesIds: List<String>,
    @SerializedName("hexagon_id")
    val hexagonId: String,
    @SerializedName("region_id")
    val regionId: Int,
    @SerializedName("score")
    val score: Int,
    @SerializedName("score_weighted1")
    val scoreWeighted1: Double,
    @SerializedName("weighted_deliveries")
    val weightedDeliveries: Double
)

data class Pagination(
    @SerializedName("next")
    val next: Int,
    @SerializedName("page")
    val page: Int,
    @SerializedName("perPage")
    val perPage: Int,
    @SerializedName("prev")
    val prev: Int,
    @SerializedName("total")
    val total: Int,
    @SerializedName("totalPage")
    val totalPage: Int
)

data class Boundary(
    @SerializedName("coordinates")
    val coordinates: List<List<List<Double>>>,
    @SerializedName("type")
    val type: String
)

