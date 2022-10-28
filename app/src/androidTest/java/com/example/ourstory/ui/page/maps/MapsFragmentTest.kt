package com.example.ourstory.ui.page.maps

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.example.ourstory.R
import com.example.ourstory.ui.page.map.MapFragment
import com.example.ourstory.utils.Constants.BASE_URL_MOCK
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
@HiltAndroidTest
class MapsFragmentTest {

    private val mockWebServer = MockWebServer()

    @Before
    fun setup() {
        mockWebServer.start(8080)
        BASE_URL_MOCK = "http://127.0.0.1:808"
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun launchMapFragment_Success() {
        launchFragmentInHiltContainer<MapFragment>()

        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(JsonConverter.readStringFromFile("mock_story_success.json"))
        mockWebServer.enqueue(mockResponse)

        onView(withId(R.id.maps)).check(matches(isDisplayed()))
    }

    @Test
    fun launchMapFragment_Failed() {
        launchFragmentInHiltContainer<MapFragment>()

        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(JsonConverter.readStringFromFile("mock_story_empty.json"))
        mockWebServer.enqueue(mockResponse)

        onView(withId(R.id.tv_map_failed)).check(matches(CoreMatchers.not(isDisplayed())))
    }

    @Test
    fun launchMapFragment_Empty() {
        launchFragmentInHiltContainer<MapFragment>()

        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(JsonConverter.readStringFromFile("mock_story_empty.json"))
        mockWebServer.enqueue(mockResponse)

        onView(withId(R.id.tv_map_failed)).check(matches(CoreMatchers.not(isDisplayed())))
    }
}