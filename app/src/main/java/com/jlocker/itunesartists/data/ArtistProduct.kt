package com.jlocker.itunesartists.data

import com.google.gson.annotations.SerializedName
import java.time.LocalDate
import java.time.LocalDateTime

data class ArtistProduct(
    @field:SerializedName("artistName") val artistName: String,
    @field:SerializedName("trackName") val trackName: String,
    @field:SerializedName("trackId") val trackId: Long,
    @field:SerializedName("releaseDate") val releaseDateAsString: String,
    @field:SerializedName("primaryGenreName") val primaryGenreName: String,
    @field:SerializedName("kind") val kind: String,
    @field:SerializedName("wrapperType") val wrapperType: String,
    @field:SerializedName("trackPrice") val trackPrice: Double
) {
    val releaseDateNoTime: String
        get() = releaseDateAsString.split("T")[0]
}