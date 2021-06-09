package com.jlocker.itunesartists.interfaces

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class IArtistsServiceTest {

    private lateinit var artistsService: IArtistsService

    @Before
    fun setUp() {
        artistsService = IArtistsService.create()
    }

    @Test
    fun `test search tracks`() {
        runBlocking {
            launch {
                val limit = 200
                val response = artistsService.searchTerm("The Killers", limit)
                assertEquals(limit, response.resultCount, "Unexpected result count.")
            }
        }
    }
}