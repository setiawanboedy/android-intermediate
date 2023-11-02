package com.example.ourstory.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.ourstory.domain.model.StoryModel
import com.example.ourstory.domain.params.StoryParams
import com.example.ourstory.domain.usecase.StoriesCase
import com.example.ourstory.ui.page.home.HomeViewModel
import com.example.ourstory.ui.page.home.StoryPageAdapter
import com.example.ourstory.utils.CoroutinesRuleTest
import com.example.ourstory.utils.DataDummy
import com.example.ourstory.utils.PageDataSourceTest
import com.example.ourstory.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesRuleTest = CoroutinesRuleTest()

    private lateinit var homeViewModel: HomeViewModel

    private lateinit var storiesCase: StoriesCase
    private val storyParams = StoryParams(5, 20)

    @Before
    fun setUp() {
        storiesCase = mock(StoriesCase::class.java)
        homeViewModel = HomeViewModel(storiesCase)
    }

    @Test
    fun `Get Stories with Paging Success`() = runTest {
        val dataDummy = DataDummy.listStoriesDummy()
        val data = PageDataSourceTest.snapshot(dataDummy)

        val stories = MutableLiveData<PagingData<StoryModel>>()
        stories.value = data

        Mockito.`when`(storiesCase.getPageStory(storyParams)).thenReturn(stories)

        val actualResult = homeViewModel.getStories(storyParams).getOrAwaitValue()
        Mockito.verify(storiesCase).getPageStory(storyParams)

        advanceUntilIdle()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryPageAdapter.differCallback,
            updateCallback = noopListUpdateCallback,
            mainDispatcher = coroutinesRuleTest.testDispatcher,
            workerDispatcher = coroutinesRuleTest.testDispatcher
        )
        differ.submitData(actualResult)
        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dataDummy.size, differ.snapshot().size)
        println(differ.snapshot())
    }

    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }
}