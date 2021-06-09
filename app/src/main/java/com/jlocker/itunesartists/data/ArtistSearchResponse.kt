package com.jlocker.itunesartists.data

import com.google.gson.annotations.SerializedName

data class ArtistSearchResponse(
    @field:SerializedName("resultCount") val resultCount: Int,
    @field:SerializedName("results") val results: List<ArtistProduct>
)