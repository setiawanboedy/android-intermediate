package com.example.ourstory.domain.usecase

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.example.ourstory.core.Sealed
import com.example.ourstory.domain.model.StoryModel
import com.example.ourstory.domain.params.MapParams
import com.example.ourstory.domain.params.StoryParams
import com.example.ourstory.domain.repository.Repository
import com.example.ourstory.domain.response.StoriesResponse
import javax.inject.Inject

class StoriesCase @Inject constructor(
    private val repository: Repository,
) : UseCase<StoriesResponse, MapParams> {
    fun getPageStory(params: StoryParams): LiveData<PagingData<StoryModel>> =
        repository.getStories(params)

    override fun call(req: MapParams): LiveData<Sealed<StoriesResponse>> =
        repository.getStoriesLocation(req)

}