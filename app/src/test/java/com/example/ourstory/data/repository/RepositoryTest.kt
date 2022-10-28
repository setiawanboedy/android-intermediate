package com.example.ourstory.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.ourstory.core.ErrorParser
import com.example.ourstory.core.SafeResponse
import com.example.ourstory.core.Sealed
import com.example.ourstory.core.Status
import com.example.ourstory.data.datasource.DataSources
import com.example.ourstory.data.network.get.GetService
import com.example.ourstory.data.network.post.AuthService
import com.example.ourstory.data.network.post.PostService
import com.example.ourstory.domain.model.StoryModel
import com.example.ourstory.domain.params.MapParams
import com.example.ourstory.domain.params.StoryParams
import com.example.ourstory.domain.request.LoginRequest
import com.example.ourstory.domain.request.RegisterRequest
import com.example.ourstory.domain.response.StoriesResponse
import com.example.ourstory.ui.page.home.StoryPageAdapter
import com.example.ourstory.utils.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RepositoryTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesRuleTest = CoroutinesRuleTest()

    @Mock
    private lateinit var source: DataSources

    @Mock
    private lateinit var sourcesMock: DataSources

    @Mock
    private lateinit var repoMock: RepositoryImpl

    private val registerRequest = RegisterRequest("Sinaga", "sinaga@gmail.com", "sinagarendi")
    private val registerResponse = DataDummy.registerResponseDummy()
    private val registerErrorDummy = DataDummy.registerErrorDummy()

    private val loginRequest = LoginRequest("sinaga@gmail.com", "sinagarendi")
    private val loginResponse = DataDummy.loginResponseDummy()
    private val loginErrorDummy = DataDummy.loginErrorDummy()

    private val storiesDummy = DataDummy.storiesResponseDummy()
    private val storiesErrorDummy = DataDummy.storiesErrorDummy()

    private val uploadResponseDummy = DataDummy.addStoryResponseDummy()

    private val descDummy = DataDummy.requestBodyDummy()
    private val multipartDummy = DataDummy.multipartFileDummy()

    @Before
    fun setUp() {
        val authService = mock(AuthService::class.java)
        val postService = mock(PostService::class.java)
        val getService = mock(GetService::class.java)
        val safeResponse = mock(SafeResponse::class.java)
        val converter = mock(ErrorParser::class.java)

        source = DataSources(safeResponse, converter, authService, getService, postService)
        sourcesMock = mock(DataSources::class.java)
        repoMock = mock(RepositoryImpl::class.java)
    }

    @Test
    fun `User Register Success`() = runBlocking {
        val service = ServiceDummy()
        val expectation = registerResponse
        val actual = service.register(registerRequest).body()
        Assert.assertNotNull(actual)
        Assert.assertEquals(expectation, actual)
    }

    @Test
    fun `User Register Error`() {
        val service = ServiceDummy()
        val expectation = registerErrorDummy
        val actual = service.registerError()
        Assert.assertNotNull(actual)
        Assert.assertEquals(expectation, actual)
    }

    @Test
    fun `User Login Success`() = runBlocking {
        val service = ServiceDummy()
        val expectation = loginResponse
        val actual = service.login(loginRequest).body()
        Assert.assertNotNull(actual)
        Assert.assertEquals(expectation, actual)
    }

    @Test
    fun `User Login Error`() {
        val service = ServiceDummy()
        val expectation = loginErrorDummy
        val actual = service.loginError()
        Assert.assertNotNull(actual)
        Assert.assertEquals(expectation, actual)
    }

    @Test
    fun `Get stories with location success`() {
        val expectation = MutableLiveData<Sealed<StoriesResponse>>()
        expectation.value = Sealed.success(storiesDummy)

        val params = MapParams(location = 1)
        `when`(repoMock.getStoriesLocation(params)).thenReturn(expectation)

        val actual = repoMock.getStoriesLocation(params).getOrAwaitValue()
        Mockito.verify(repoMock).getStoriesLocation(params)
        if (actual.status == Status.SUCCESS) {
            Assert.assertNotNull(actual)
            Assert.assertEquals(expectation.value, actual)
        }
    }

    @Test
    fun `Get stories with location error`() {
        val expectation = MutableLiveData<Sealed<StoriesResponse>>()
        expectation.value = Sealed.error(storiesErrorDummy.message, null)
        val params = MapParams(location = 1)
        `when`(repoMock.getStoriesLocation(params)).thenReturn(expectation)

        val actual = repoMock.getStoriesLocation(params).getOrAwaitValue()
        Mockito.verify(repoMock).getStoriesLocation(params)
        if (actual.status == Status.ERROR) {
            Assert.assertNotNull(actual)
            Assert.assertEquals(expectation.value, actual)
        }
    }

    @Test
    fun `Upload story success`() = runBlocking {
        val service = ServiceDummy()
        val expectation = uploadResponseDummy
        val actual = service.addStory(descDummy, multipartDummy, null, null).body()

        Assert.assertNotNull(actual)
        Assert.assertEquals(expectation, actual)
    }

    @Test
    fun `Upload story error`() {
        val service = ServiceDummy()
        val expected = DataDummy.uploadErrorDummy()
        val actual = service.uploadError()

        Assert.assertNotNull(actual)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `Get Stories page success`() = runBlocking {
        val dataDummy = DataDummy.listStoriesDummy()
        val data = PageDataSourceTest.snapshot(dataDummy)

        val expectation = MutableLiveData<PagingData<StoryModel>>()
        expectation.value = data

        val response = DataDummy.storiesResponseDummy()
        val storyParams = StoryParams(5, 20)
        `when`(repoMock.getStories(storyParams)).thenReturn(expectation)

        val actual = repoMock.getStories(storyParams).getOrAwaitValue()
        Mockito.verify(repoMock).getStories(storyParams)

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryPageAdapter.differCallback,
            updateCallback = listUpdateCallback,
            mainDispatcher = coroutinesRuleTest.testDispatcher,
            workerDispatcher = coroutinesRuleTest.testDispatcher
        )

        differ.submitData(actual)
        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(response.listStory.size, differ.snapshot().size)
    }

    private val listUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {

        }

        override fun onRemoved(position: Int, count: Int) {

        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {

        }

        override fun onChanged(position: Int, count: Int, payload: Any?) {

        }
    }
}