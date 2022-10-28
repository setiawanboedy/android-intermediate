package com.example.ourstory.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.ourstory.core.Sealed
import com.example.ourstory.core.Status
import com.example.ourstory.domain.request.AddRequest
import com.example.ourstory.domain.response.GenericResponse
import com.example.ourstory.ui.page.add.AddViewModel
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
import java.io.File

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AddViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesRuleTest = CoroutinesRuleTest()

    @Mock
    lateinit var addViewModel: AddViewModel

    private val file = File("image")
    private val addRequest = AddRequest(file, "desc", null, null)

    @Test
    fun `Add story success`() = runTest {
        val expectation = DataDummy.addStoryResponseDummy()

        val data = MutableLiveData<Sealed<GenericResponse>>()
        data.value = Sealed.success(expectation)

        Mockito.`when`(addViewModel.addStory(addRequest)).thenReturn(data)
        val actual = addViewModel.addStory(addRequest).getOrAwaitValue()
        Mockito.verify(addViewModel).addStory(addRequest)

        advanceUntilIdle()

        Assert.assertNotNull(actual)
        Assert.assertEquals(actual.data, expectation)
    }

    @Test
    fun `Add story error`() = runTest {

        val data = MutableLiveData<Sealed<GenericResponse>>()
        data.value = Sealed.error("UNKNOWN ERROR", null)

        Mockito.`when`(addViewModel.addStory(addRequest)).thenReturn(data)
        val actual = addViewModel.addStory(addRequest).getOrAwaitValue()
        Mockito.verify(addViewModel).addStory(addRequest)

        advanceUntilIdle()

        Assert.assertNotNull(actual)
        Assert.assertTrue(actual.status == Status.ERROR)
    }
}