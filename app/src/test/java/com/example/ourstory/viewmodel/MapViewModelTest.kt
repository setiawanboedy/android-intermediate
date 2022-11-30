package com.example.ourstory.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.ourstory.core.Sealed
import com.example.ourstory.core.Status
import com.example.ourstory.domain.params.MapParams
import com.example.ourstory.domain.usecase.StoriesCase
import com.example.ourstory.ui.page.map.MapViewModel
import com.example.ourstory.utils.CoroutinesRuleTest
import com.example.ourstory.utils.DataDummy
import com.example.ourstory.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MapViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesRuleTest = CoroutinesRuleTest()

    private lateinit var mapViewModel: MapViewModel
    private lateinit var storiesCase: StoriesCase

    private val mapParams = MapParams(location = 1)

    @Before
    fun setUp() {
        storiesCase = Mockito.mock(StoriesCase::class.java)
        mapViewModel = MapViewModel(storiesCase)
    }

    @Test
    fun `Get Stories with Location Success`() = runTest {
        val expectation = flowOf(
            Sealed.success(DataDummy.storiesResponseDummy())
        )

        Mockito.`when`(storiesCase.call(mapParams)).thenReturn(expectation)

        val actual = mapViewModel.getStoriesLocation(mapParams).getOrAwaitValue()
        Mockito.verify(storiesCase).call(mapParams)

        advanceUntilIdle()

        Assert.assertNotNull(actual)
        Assert.assertTrue(actual.status == Status.SUCCESS)
        expectation.collect { res ->
            Assert.assertEquals(res.data, actual.data)
        }
    }

    @Test
    fun `Get Stories with Location Error`() = runTest {
        val expectation = flowOf(
            Sealed.error("Invalid token signature", null)
        )

        Mockito.`when`(storiesCase.call(mapParams)).thenReturn(expectation)

        val actual = mapViewModel.getStoriesLocation(mapParams).getOrAwaitValue()
        Mockito.verify(storiesCase).call(mapParams)

        advanceUntilIdle()
        Assert.assertNotNull(actual)
        Assert.assertTrue(actual.status == Status.ERROR)
        expectation.collect { res ->
            Assert.assertEquals(res.message, actual.message)
        }
    }


}