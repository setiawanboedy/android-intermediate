package com.example.ourstory.ui.page.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.ourstory.core.Sealed
import com.example.ourstory.domain.params.MapParams
import com.example.ourstory.domain.response.StoriesResponse
import com.example.ourstory.domain.usecase.StoriesCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val story: StoriesCase
) : ViewModel() {
    fun getStoriesLocation(params: MapParams): LiveData<Sealed<StoriesResponse>> =
        story.call(params).asLiveData()
}