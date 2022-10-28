package com.example.ourstory.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.ourstory.core.Sealed
import com.example.ourstory.core.Status
import com.example.ourstory.domain.params.MapParams
import com.example.ourstory.domain.response.StoriesResponse
import com.example.ourstory.ui.page.map.MapViewModel
import com.example.ourstory.utils.CoroutinesRuleTest
import com.example.ourstory.utils.DataDummy
import com.example.ourstory.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MapViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesRuleTest = CoroutinesRuleTest()

    @Mock
    lateinit var mapViewModel: MapViewModel

    private val storiesResponseDummy = DataDummy.storiesResponseDummy()
    private val mapParams = MapParams(location = 1)

    @Test
    fun `Get stories with location success`() = runTest {
        val expectation = storiesResponseDummy
        val data = MutableLiveData<Sealed<StoriesResponse>>()
        data.value = Sealed.success(expectation)

        Mockito.`when`(mapViewModel.getStoriesLocation(mapParams)).thenReturn(data)

        val actual = mapViewModel.getStoriesLocation(mapParams).getOrAwaitValue()
        Mockito.verify(mapViewModel).getStoriesLocation(mapParams)

        advanceUntilIdle()

        Assert.assertNotNull(actual)
        Assert.assertEquals(actual.data, expectation)
    }

    @Test
    fun `Get stories with location error`() = runTest {
        val data = MutableLiveData<Sealed<StoriesResponse>>()
        data.value = Sealed.error("UNKNOWN ERROR", null)

        Mockito.`when`(mapViewModel.getStoriesLocation(mapParams)).thenReturn(data)

        val actual = mapViewModel.getStoriesLocation(mapParams).getOrAwaitValue()
        Mockito.verify(mapViewModel).getStoriesLocation(mapParams)

        advanceUntilIdle()
        if (actual.status == Status.ERROR)
            Assert.assertNotNull(actual)
        Assert.assertTrue(actual.status == Status.ERROR)
    }


}