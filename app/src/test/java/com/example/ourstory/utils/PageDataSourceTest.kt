package com.example.ourstory.utils

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.ourstory.domain.model.StoryModel

class PageDataSourceTest : PagingSource<Int, LiveData<List<StoryModel>>>() {

    companion object {
        fun snapshot(items: List<StoryModel>): PagingData<StoryModel> =
            PagingData.from(items)
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<StoryModel>>>): Int = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<StoryModel>>> =
        LoadResult.Page(emptyList(), 0, 1)
}