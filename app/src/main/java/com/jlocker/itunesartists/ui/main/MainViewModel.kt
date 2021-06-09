package com.jlocker.itunesartists.ui.main

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.jlocker.itunesartists.data.ArtistProduct
import com.jlocker.itunesartists.data.ArtistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: ArtistRepository
): ViewModel() {
    val isLoading: ObservableField<Boolean> = ObservableField()
    val searchString: ObservableField<String> = ObservableField()
    val artistProductLiveData: MutableLiveData<PagingData<ArtistProduct>> = MutableLiveData()

    private var searchJob: Job? = null

    fun searchTracks() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            searchString.get()?.let { text ->
                repository.getSearchResultStream(text).cachedIn(viewModelScope).collectLatest {
                    artistProductLiveData.value = it
                }
            }
        }
    }

}