package com.jlocker.itunesartists.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.jlocker.itunesartists.interfaces.IArtistsService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ArtistRepository @Inject constructor(private val service: IArtistsService) {

    companion object {
        private const val PAGE_SIZE = 25
    }

    fun getSearchResultStream(artistName: String) : Flow<PagingData<ArtistProduct>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = PAGE_SIZE),
            pagingSourceFactory = { ArtistTracksPagingSource(service, artistName) }
        ).flow
    }
}