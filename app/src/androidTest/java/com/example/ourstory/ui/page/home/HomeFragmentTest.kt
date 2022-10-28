package com.example.ourstory.ui.page.home

import androidx.paging.ExperimentalPagingApi
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.example.ourstory.MainActivity
import com.example.ourstory.R
import com.example.ourstory.utils.Constants.BASE_URL_MOCK
import com.example.ourstory.utils.EspressoIdlingResource
import com.example.ourstory.utils.JsonConverter
import com.example.ourstory.utils.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers
import org.junit.After
import org.junit.Before
import org.junit.Test

@MediumTest
@ExperimentalPagingApi
@HiltAndroidTest
class HomeFragmentTest {

    private val mockWebServer = MockWebServer()

    @Before
    fun setup() {
        ActivityScenario.launch(MainActivity::class.java)
        mockWebServer.start(8080)
        BASE_URL_MOCK = "http://127.0.0.1:8080/"
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun launchHomeFragment_Success() {
        launchFragmentInHiltContainer<HomeFragment>()

        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(JsonConverter.readStringFromFile("mock_story_success.json"))
        mockWebServer.enqueue(mockResponse)

        onView(withId(R.id.rv_stories)).check(matches(isDisplayed()))
    }

    @Test
    fun launchHomeFragment_Failed() {
        launchFragmentInHiltContainer<HomeFragment>()

        val mockResponse = MockResponse()
            .setResponseCode(500)
            .setBody(JsonConverter.readStringFromFile("mock_story_empty.json"))
        mockWebServer.enqueue(mockResponse)

        onView(withId(R.id.tv_empty)).check(matches(CoreMatchers.not(isDisplayed())))
    }

    @Test
    fun launcherHomeFragment_Empty() {
        launchFragmentInHiltContainer<HomeFragment>()

        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(JsonConverter.readStringFromFile("mock_story_empty.json"))
        mockWebServer.enqueue(mockResponse)

        onView(withId(R.id.tv_empty)).check(matches(CoreMatchers.not(isDisplayed())))
    }
}