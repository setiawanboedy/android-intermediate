package com.example.ourstory.data.datasource

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.ourstory.data.local.db.StoryDatabase
import com.example.ourstory.data.local.entity.RemoteKeyData
import com.example.ourstory.data.network.get.GetService
import com.example.ourstory.domain.model.StoryModel
import com.example.ourstory.utils.Constants.INIT_PAGE_INDEX
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class PageDataSourceMediator @Inject constructor(
    private val db: StoryDatabase,
    private val getService: GetService
) : RemoteMediator<Int, StoryModel>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, StoryModel>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKey = getRemoteKeyToCurrentPosition(state)
                remoteKey?.nextKey?.minus(1) ?: INIT_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKey = getRemoteKeyFirstItem(state)
                val prevKey = remoteKey?.prevKey ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKey != null
                )
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKey = getRemoteKeyLastItem(state)
                val nextKey = remoteKey?.nextKey ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKey != null
                )
                nextKey
            }
        }
        return try {
            val response = getService.getStories(page, state.config.pageSize)
            val responseData = response.body()?.listStory ?: listOf()
            val endOfPagination = responseData.isEmpty()

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    db.remoteKeyDao().deleteRemoteKey()
                    db.storyDao().deleteAllStory()
                }

                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPagination) null else page + 1
                val keys = responseData.map {
                    RemoteKeyData(id = it.id, prevKey = prevKey, nextKey = nextKey)
                }

                db.remoteKeyDao().addAll(keys)
                db.storyDao().addStory(responseData)

            }
            MediatorResult.Success(endOfPaginationReached = endOfPagination)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    override suspend fun initialize(): InitializeAction =
        InitializeAction.LAUNCH_INITIAL_REFRESH

    private suspend fun getRemoteKeyToCurrentPosition(state: PagingState<Int, StoryModel>): RemoteKeyData? =
        state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id.let { id ->
                db.remoteKeyDao().getRemoteKeyById(id)
            }
        }

    private suspend fun getRemoteKeyFirstItem(state: PagingState<Int, StoryModel>): RemoteKeyData? =
        state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            .let { data ->
                db.remoteKeyDao().getRemoteKeyById(data?.id)
            }

    private suspend fun getRemoteKeyLastItem(state: PagingState<Int, StoryModel>): RemoteKeyData? =
        state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            .let { data ->
                db.remoteKeyDao().getRemoteKeyById(data?.id)
            }
}