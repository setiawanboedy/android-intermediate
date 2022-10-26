package com.example.ourstory.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.ourstory.core.Sealed
import com.example.ourstory.data.datasource.DataSources
import com.example.ourstory.data.datasource.PageDataSourceMediator
import com.example.ourstory.data.local.db.StoryDatabase
import com.example.ourstory.data.network.get.GetService
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
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val dataSources: DataSources,
    private val db: StoryDatabase,
    private val getService: GetService,
) : Repository {
    companion object {
        @Volatile
        private var instance: RepositoryImpl? = null
        fun getInstance(
            dataSources: DataSources,
            db: StoryDatabase,
            getService: GetService
        ): RepositoryImpl =
            instance ?: synchronized(this) {
                instance ?: RepositoryImpl(dataSources, db, getService)
            }
    }

    override fun register(req: RegisterRequest): LiveData<Sealed<RegisterResponse>> =
        dataSources.register(req).asLiveData()

    override fun login(req: LoginRequest): LiveData<Sealed<LoginResponse>> =
        dataSources.login(req).asLiveData()

    @OptIn(ExperimentalPagingApi::class)
    override fun getStories(params: StoryParams): LiveData<PagingData<StoryModel>> {
        val dataPager = Pager(
            config = PagingConfig(
                pageSize = params.page,
                maxSize = params.size,
                enablePlaceholders = false
            ),
            remoteMediator = PageDataSourceMediator(db, getService),
            pagingSourceFactory = { db.storyDao().getAllStory() }

        ).flow

        return dataPager.asLiveData()
    }

    override fun getStoriesLocation(params: MapParams): LiveData<Sealed<StoriesResponse>> =
        dataSources.getStoriesLocation(params).asLiveData()

    override fun addStory(req: AddRequest): LiveData<Sealed<GenericResponse>> =
        dataSources.addStory(req).asLiveData()
}