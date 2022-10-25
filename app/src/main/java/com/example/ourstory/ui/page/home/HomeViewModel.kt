package com.example.ourstory.ui.page.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.ourstory.core.Sealed
import com.example.ourstory.params.StoryParams
import com.example.ourstory.response.StoriesResponse
import com.example.ourstory.usecase.StoriesCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val story: StoriesCase
) : ViewModel() {
    fun getStories(params: StoryParams): LiveData<Sealed<StoriesResponse>> = story.call(params)
}