package com.example.ourstory.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import com.example.ourstory.core.Sealed
import com.example.ourstory.domain.model.StoryModel
import com.example.ourstory.domain.params.MapParams
import com.example.ourstory.domain.params.StoryParams
import com.example.ourstory.domain.repository.Repository
import com.example.ourstory.domain.request.AddRequest
import com.example.ourstory.domain.request.LoginRequest
import com.example.ourstory.domain.request.RegisterRequest
import com.example.ourstory.domain.response.GenericResponse
import com.example.ourstory.domain.response.LoginResponse
import com.example.ourstory.domain.response.RegisterResponse
import com.example.ourstory.domain.response.StoriesResponse

class RepositoryHelper : Repository {
    override suspend fun register(req: RegisterRequest): Sealed<RegisterResponse> =
        Sealed.error("\"email\" must be a valid email", null)

    override suspend fun login(req: LoginRequest): Sealed<LoginResponse> =
        Sealed.error("User not found", null)


    override fun getStories(params: StoryParams): LiveData<PagingData<StoryModel>> {
        val paging = MutableLiveData<PagingData<StoryModel>>()
        paging.value = PagingData.from(DataDummy.listStoriesDummy())
        return paging
    }


    override suspend fun getStoriesLocation(params: MapParams): Sealed<StoriesResponse> =
        Sealed.error("Invalid token signature", null)


    override suspend fun addStory(req: AddRequest): Sealed<GenericResponse> =
        Sealed.error("\"description\" must not be empty", null)


}