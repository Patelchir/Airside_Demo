package com.example.airside_demo.search

data class SearchResponse(
    val page: Int,
    val pages: Int,
    val perpage: Int,
    val photo: List<Photo>,
    val total: String
) {
    data class Photo(
        val farm: Int,
        val id: String,
        val isfamily: Int,
        val isfriend: Int,
        val ispublic: Int,
        val owner: String,
        val secret: String,
        val server: String,
        val title: String,
        var imageurl: String
    )
}