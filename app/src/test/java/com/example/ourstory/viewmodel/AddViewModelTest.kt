package com.example.ourstory.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.ourstory.core.Sealed
import com.example.ourstory.core.Status
import com.example.ourstory.domain.request.AddRequest
import com.example.ourstory.domain.usecase.AddCase
import com.example.ourstory.ui.page.add.AddViewModel
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
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import java.io.File

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AddViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesRuleTest = CoroutinesRuleTest()

    private lateinit var addViewModel: AddViewModel
    private lateinit var useCase: AddCase

    private val file = File("image")
    private val addRequest = AddRequest(file, "desc", null, null)

    @Before
    fun setUp() {

        useCase = mock(AddCase::class.java)
        addViewModel = AddViewModel(useCase)
    }

    @Test
    fun `Add Story Success`() = runTest {
        val expectation = flowOf(
            Sealed.success(DataDummy.addStoryResponseDummy())
        )

        Mockito.`when`(useCase.call(addRequest)).thenReturn(expectation)
        val actual = addViewModel.addStory(addRequest).getOrAwaitValue()
        Mockito.verify(useCase).call(addRequest)

        advanceUntilIdle()

        Assert.assertNotNull(actual)
        Assert.assertTrue(actual.status == Status.SUCCESS)
        expectation.collect { expect ->
            Assert.assertEquals(expect.data, actual.data)
        }

    }

    @Test
    fun `Add Story Error`() = runTest {
        val expectation = flowOf(
            Sealed.error("", null)
        )

        Mockito.`when`(useCase.call(addRequest)).thenReturn(expectation)
        val actual = addViewModel.addStory(addRequest).getOrAwaitValue()
        Mockito.verify(useCase).call(addRequest)

        advanceUntilIdle()

        Assert.assertNotNull(actual)
        Assert.assertTrue(actual.status == Status.ERROR)
    }
}