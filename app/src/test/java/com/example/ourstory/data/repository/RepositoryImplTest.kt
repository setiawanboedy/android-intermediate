package com.example.ourstory.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.ourstory.core.ErrorParser
import com.example.ourstory.core.SafeResponse
import com.example.ourstory.core.Sealed
import com.example.ourstory.core.Status
import com.example.ourstory.data.datasource.DataSources
import com.example.ourstory.data.local.db.StoryDatabase
import com.example.ourstory.data.network.get.GetService
import com.example.ourstory.data.network.post.AuthService
import com.example.ourstory.data.network.post.PostService
import com.example.ourstory.domain.model.StoryModel
import com.example.ourstory.domain.params.MapParams
import com.example.ourstory.domain.params.StoryParams
import com.example.ourstory.domain.repository.Repository
import com.example.ourstory.domain.request.AddRequest
import com.example.ourstory.domain.request.LoginRequest
import com.example.ourstory.domain.request.RegisterRequest
import com.example.ourstory.domain.response.GenericResponse
import com.example.ourstory.session.SessionManager
import com.example.ourstory.ui.page.home.StoryPageAdapter
import com.example.ourstory.utils.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.robolectric.RobolectricTestRunner
import java.io.File


@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class RepositoryImplTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesRuleTest = CoroutinesRuleTest()

    private lateinit var authService: AuthService
    private lateinit var postService: PostService
    private lateinit var getService: GetService
    private lateinit var safeResponse: SafeResponse
    private lateinit var converter: ErrorParser

    private lateinit var repoMock: Repository

    private lateinit var database: StoryDatabase

    private lateinit var photo: File

    private lateinit var sources: DataSources
    private lateinit var repo: RepositoryImpl


    private val uploadResponseDummy = DataDummy.addStoryResponseDummy()

    private val descDummy = DataDummy.requestBodyDummy()
    private val multipartDummy = DataDummy.multipartFileDummy()

    @Before
    fun setUp() {
        val session = mock(SessionManager::class.java)
        authService = Helper.authService()
        postService = Helper.postService()
        getService = Helper.getService(session)
        safeResponse = Helper.safeResponse()
        converter = Helper.errorParser()
        database = Helper.getDatabase()
        sources = DataSources(safeResponse, converter, authService, getService, postService)
        repo = RepositoryImpl(sources, database, getService)
//        photo = Helper.getPhoto()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `User Register Success`() = runTest {
        val randomString = java.util.UUID.randomUUID().toString()
        val registerRequest =
            RegisterRequest("Sinaga", "sinaga${randomString}@gmail.com", "sinagarendi")
        val expectation = DataDummy.registerResponseDummy()
        val actual = repo.register(registerRequest)
        Assert.assertTrue(actual.status == Status.SUCCESS)
        Assert.assertEquals(expectation.message, actual.data?.message)
    }

    @Test
    fun `User Register Error - Invalid email`() = runBlocking {
        val registerRequest = RegisterRequest("rendi", "sinagagmail.com", "sinagarendi")
        val expectation = DataDummy.registerErrorDummy()
        val actual = repo.register(registerRequest)
        Assert.assertTrue(actual.status == Status.ERROR)
        Assert.assertEquals(expectation.message, actual.message)

    }

    @Test
    fun `User Login Success`() = runTest {
        val loginRequest = LoginRequest("sinaga@gmail.com", "sinagarendi")
        val expectation = DataDummy.loginResponseDummy()
        val actual = repo.login(loginRequest)
        println(actual)
        Assert.assertTrue(actual.status == Status.SUCCESS)
        Assert.assertEquals(expectation.message, actual.data?.message)
    }

    @Test
    fun `User Login Error - User not found`() = runBlocking {
        val expectation = DataDummy.loginErrorDummy()
        val actual = repo.login(LoginRequest("jldjfo@gmail.com", "sfjlskdjlf"))
        Assert.assertTrue(actual.status == Status.ERROR)
        Assert.assertEquals(expectation.message, actual.message)
    }

    @Test
    fun `Get stories with location success`() = runBlocking {
        val expectation = DataDummy.storiesResponseDummy()

        val params = MapParams(location = 1)

        val actual = repo.getStoriesLocation(params)
        Assert.assertTrue(actual.status == Status.SUCCESS)
        Assert.assertEquals(expectation.message, actual.data?.message)
    }

    @Test
    fun `Get stories with location error - wrong token`() = runBlocking {
        val expectation = DataDummy.storiesErrorDummy()
        val params = MapParams(location = 0)
        val actual = repo.getStoriesLocation(params)
        println(actual)
    }

    @Test
    fun `Upload Story Success`() = runBlocking {


        val addRequest = AddRequest(photo, "description", null, null)
        val expectation = MutableLiveData<Sealed<GenericResponse>>()
//        expectation.value =
//            Sealed.success(service.addStory(descDummy, multipartDummy, null, null).body())
//        val actual = repo.addStory(addRequest)
//
//        Assert.assertNotNull(actual)
//        if (actual.status == Status.SUCCESS) {
//            Assert.assertNotNull(actual)
//            Assert.assertEquals(expectation.value, actual)
//        }
    }

    @Test
    fun `Upload story error`() = runBlocking {
//        val service = ServiceDummy()
//        val expected = DataDummy.uploadErrorDummy()
//        val actual = service.uploadError()
//
//        Assert.assertNotNull(actual)
//        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `Get Stories with page success`() = runBlocking {
        val dataDummy = DataDummy.listStoriesDummy()
        val data = PageDataSourceTest.snapshot(dataDummy)

        val liveData = MutableLiveData<PagingData<StoryModel>>()
        liveData.value = data

        val storyParams = StoryParams(5, 20)

        `when`(repoMock.getStories(storyParams))
            .thenReturn(liveData as LiveData<PagingData<StoryModel>>)

        val actual = repoMock.getStories(storyParams).getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryPageAdapter.differCallback,
            updateCallback = listUpdateCallback,
            mainDispatcher = coroutinesRuleTest.testDispatcher,
            workerDispatcher = coroutinesRuleTest.testDispatcher
        )

        differ.submitData(actual)
        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dataDummy.size, differ.snapshot().size)
    }

    private val listUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }
}