package com.jlocker.itunesartists.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.jlocker.itunesartists.interfaces.IArtistsService

class ArtistTracksPagingSource(private val service: IArtistsService, private val artistName: String) : PagingSource<Int, ArtistProduct>() {

    companion object {
        private const val START_PAGE = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArtistProduct> {
        val page = params.key ?: START_PAGE

        return try {
            val response = service.searchTerm(artistName, 200)
            val tracks = response.results.distinctBy { it.trackName + it.artistName }
                .filter { it.wrapperType == "track" &&
                    (artistName.toLowerCase() in it.artistName.toLowerCase())}
            LoadResult.Page(
                data = tracks,
                prevKey = null,
                nextKey = null
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ArtistProduct>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}