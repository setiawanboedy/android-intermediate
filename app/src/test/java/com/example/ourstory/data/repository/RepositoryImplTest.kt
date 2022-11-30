package com.example.ourstory.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.ourstory.core.ErrorParser
import com.example.ourstory.core.SafeResponse
import com.example.ourstory.core.Status
import com.example.ourstory.data.datasource.DataSources
import com.example.ourstory.data.local.db.StoryDatabase
import com.example.ourstory.domain.model.StoryModel
import com.example.ourstory.domain.params.MapParams
import com.example.ourstory.domain.params.StoryParams
import com.example.ourstory.domain.request.AddRequest
import com.example.ourstory.domain.request.LoginRequest
import com.example.ourstory.domain.request.RegisterRequest
import com.example.ourstory.ui.page.home.StoryPageAdapter
import com.example.ourstory.utils.*
import com.example.ourstory.utils.service.FakeAuthService
import com.example.ourstory.utils.service.FakeGetService
import com.example.ourstory.utils.service.FakePostService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.robolectric.RobolectricTestRunner
import retrofit2.Retrofit
import java.io.File


@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class RepositoryImplTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesRuleTest = CoroutinesRuleTest()


    private lateinit var authService: FakeAuthService
    private lateinit var postService: FakePostService
    private lateinit var getService: FakeGetService
    private lateinit var safeResponse: SafeResponse
    private lateinit var converter: ErrorParser

    private lateinit var database: StoryDatabase

    private lateinit var sources: DataSources
    private lateinit var repo: RepositoryImpl

    @Before
    fun setUp() {
        val retrofit = mock(Retrofit::class.java)
        authService = FakeAuthService()
        postService = FakePostService()
        getService = FakeGetService()

        safeResponse = SafeResponse()
        converter = ErrorParser(retrofit)
        database = Helper.getDatabase()

        sources = DataSources(safeResponse, converter, authService, getService, postService)
        repo = RepositoryImpl(sources, database, getService)

    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `User Register Success`() = runTest {

        val registerRequest =
            RegisterRequest("Sinaga", "sinaga@gmail.com", "sinagarendi")
        val expectation = DataDummy.registerResponseDummy()
        val actual = repo.register(registerRequest)
        Assert.assertNotNull(actual)
        Assert.assertTrue(actual.status == Status.SUCCESS)
        Assert.assertEquals(expectation.message, actual.data?.message)
    }

    @Test
    fun `User Register Error - Invalid email`() = runTest {
        val repository = RepositoryHelper()
        val registerRequest = RegisterRequest("rendi", "sinagagmail", "sinagarendi")
        val expectation = DataDummy.registerErrorDummy()
        val actual = repository.register(registerRequest)
        Assert.assertNotNull(actual)
        Assert.assertTrue(actual.status == Status.ERROR)
        Assert.assertEquals(expectation.message, actual.message)

    }

    @Test
    fun `User Login Success`() = runTest {
        val loginRequest = LoginRequest("sinaga@gmail.com", "sinagarendi")
        val expectation = DataDummy.loginResponseDummy()
        val actual = repo.login(loginRequest)
        Assert.assertNotNull(actual)
        Assert.assertTrue(actual.status == Status.SUCCESS)
        Assert.assertEquals(expectation.message, actual.data?.message)
    }

    @Test
    fun `User Login Error - User not found`() = runTest {
        val repository = RepositoryHelper()
        val expectation = DataDummy.loginErrorDummy()
        val actual = repository.login(LoginRequest("jldjfo@gmail.com", "sfjlskdjlf"))
        Assert.assertNotNull(actual)
        Assert.assertTrue(actual.status == Status.ERROR)
        Assert.assertEquals(expectation.message, actual.message)
    }

    @Test
    fun `Get stories with location success`() = runBlocking {
        val expectation = DataDummy.storiesResponseDummy()

        val params = MapParams(location = 1)

        val actual = repo.getStoriesLocation(params)
        Assert.assertNotNull(actual)
        Assert.assertTrue(actual.status == Status.SUCCESS)
        Assert.assertEquals(expectation.message, actual.data?.message)
    }

    @Test
    fun `Get stories with location error - wrong token`() = runTest {
        val repository = RepositoryHelper()
        val expectation = DataDummy.storiesErrorDummy()
        val params = MapParams(location = 0)
        val actual = repository.getStoriesLocation(params)
        Assert.assertNotNull(actual)
        Assert.assertTrue(actual.status == Status.ERROR)
        Assert.assertEquals(expectation.message, actual.message)
    }

    @Test
    fun `Upload Story Success`() = runTest {
        val addRequest = AddRequest(File("photo"), "description", null, null)
        val expectation = DataDummy.addStoryResponseDummy()
        val actual = repo.addStory(addRequest)
        Assert.assertNotNull(actual)
        Assert.assertTrue(actual.status == Status.SUCCESS)
        Assert.assertEquals(expectation.message, actual.data?.message)
    }

    @Test
    fun `Upload story error - empty description`() = runTest {
        val repository = RepositoryHelper()
        val addRequest = AddRequest(File("photo"), "description", null, null)
        val expected = DataDummy.addErrorDummy()
        val actual = repository.addStory(addRequest)
        Assert.assertNotNull(actual)
        Assert.assertTrue(actual.status == Status.ERROR)
        Assert.assertEquals(expected.message, actual.message)
    }

    @Test
    fun `Get Stories with page success`() = runTest {
        val repository = RepositoryHelper()

        val dataDummy = DataDummy.listStoriesDummy()
        val data = PageDataSourceTest.snapshot(dataDummy)

        val liveData = MutableLiveData<PagingData<StoryModel>>()
        liveData.value = data

        val storyParams = StoryParams(5, 20)

        val actual = repository.getStories(storyParams).getOrAwaitValue()
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