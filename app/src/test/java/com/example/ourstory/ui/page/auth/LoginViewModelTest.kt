package com.example.ourstory.ui.page.auth

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.ourstory.core.Sealed
import com.example.ourstory.core.Status
import com.example.ourstory.domain.request.LoginRequest
import com.example.ourstory.domain.response.LoginResponse
import com.example.ourstory.domain.usecase.LoginCase
import com.example.ourstory.domain.usecase.RegisterCase
import com.example.ourstory.ui.utils.DataDummyTest
import com.example.ourstory.utils.CoroutinesRuleTest
import com.example.ourstory.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutinesRuleTest = CoroutinesRuleTest()

    @Mock
    private lateinit var loginCase: LoginCase

    @Mock
    private lateinit var registerCase: RegisterCase

    private lateinit var authViewModel: AuthViewModel

    @Before
    fun setUp() {
        authViewModel = AuthViewModel(loginCase, registerCase)
    }

    @Test
    fun `Login success or not null`() = runTest {
        val loginRequest = LoginRequest(email = "reina@dicoding.org", password = "1234")
        val loginResponse = DataDummyTest.loginResponseSuccessTest()

//        jika menggunakan live data di repository
        val expectationLiveData = MutableLiveData<Sealed<LoginResponse>>()
        expectationLiveData.value = Sealed.success(loginResponse)

//        jika menggunakan flow di repository
        val expectation = flow {
            emit(Sealed.success(loginResponse))
        }

        `when`(loginCase.call(loginRequest)).thenReturn(expectation)
        val actual = authViewModel.login(loginRequest).getOrAwaitValue()

        Assert.assertNotNull(actual)
        Assert.assertTrue(actual.status == Status.SUCCESS)
        Assert.assertEquals(expectationLiveData.value!!.message, actual.data?.message)
    }

    @Test
    fun `Register success or not null`() {
    }
}