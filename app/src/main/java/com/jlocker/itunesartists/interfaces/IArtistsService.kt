package com.jlocker.itunesartists.interfaces

import com.jlocker.itunesartists.data.ArtistSearchResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface IArtistsService {

    companion object {
        private const val BASE_URL = "https://itunes.apple.com/"

        fun create(): IArtistsService {
            val client = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC })
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(IArtistsService::class.java)
        }
    }

    @GET("search")
    suspend fun searchTerm(@Query("term") term: String, @Query("limit") limit: Int) : ArtistSearchResponse
}