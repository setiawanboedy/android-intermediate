package com.example.ourstory.usecase

import androidx.lifecycle.LiveData
import com.example.ourstory.core.Sealed
import com.example.ourstory.params.StoryParams
import com.example.ourstory.repository.Repository
import com.example.ourstory.response.StoriesResponse
import javax.inject.Inject

class StoriesCase @Inject constructor(
    private val repository: Repository,
) : UseCase<StoriesResponse, StoryParams> {
    override fun call(params: StoryParams): LiveData<Sealed<StoriesResponse>> =
        repository.getStories(params)

}