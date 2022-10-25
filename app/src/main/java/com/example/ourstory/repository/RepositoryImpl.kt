package com.example.ourstory.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.ourstory.core.Sealed
import com.example.ourstory.datasource.DataSources
import com.example.ourstory.params.StoryParams
import com.example.ourstory.request.AddRequest
import com.example.ourstory.request.LoginRequest
import com.example.ourstory.request.RegisterRequest
import com.example.ourstory.response.GenericResponse
import com.example.ourstory.response.LoginResponse
import com.example.ourstory.response.RegisterResponse
import com.example.ourstory.response.StoriesResponse
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val dataSources: DataSources,
) : Repository {
    companion object {
        @Volatile
        private var instance: RepositoryImpl? = null
        fun getInstance(
            dataSources: DataSources
        ): RepositoryImpl =
            instance ?: synchronized(this) {
                instance ?: RepositoryImpl(dataSources)
            }
    }

    override fun register(req: RegisterRequest): LiveData<Sealed<RegisterResponse>> =
        dataSources.register(req).asLiveData()

    override fun login(req: LoginRequest): LiveData<Sealed<LoginResponse>> =
        dataSources.login(req).asLiveData()

    override fun getStories(params: StoryParams): LiveData<Sealed<StoriesResponse>> =
        dataSources.getStories(params).asLiveData()

    override fun addStory(req: AddRequest): LiveData<Sealed<GenericResponse>> =
        dataSources.addStory(req).asLiveData()
}