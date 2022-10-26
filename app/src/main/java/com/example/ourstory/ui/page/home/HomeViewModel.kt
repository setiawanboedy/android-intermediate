package com.example.ourstory.ui.page.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.example.ourstory.domain.model.StoryModel
import com.example.ourstory.domain.params.StoryParams
import com.example.ourstory.domain.usecase.StoriesCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val story: StoriesCase
) : ViewModel() {
    fun getStories(params: StoryParams): LiveData<PagingData<StoryModel>> =
        story.getPageStory(params)
}