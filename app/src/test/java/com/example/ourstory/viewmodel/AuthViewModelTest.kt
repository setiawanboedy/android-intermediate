package com.example.ourstory.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.ourstory.core.Sealed
import com.example.ourstory.core.Status
import com.example.ourstory.domain.request.LoginRequest
import com.example.ourstory.domain.request.RegisterRequest
import com.example.ourstory.domain.usecase.LoginCase
import com.example.ourstory.domain.usecase.RegisterCase
import com.example.ourstory.ui.page.auth.AuthViewModel
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

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AuthViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesRuleTest = CoroutinesRuleTest()

    private lateinit var loginCase: LoginCase
    private lateinit var registerCase: RegisterCase
    private lateinit var authViewModel: AuthViewModel

    private val loginRequest = LoginRequest("sinaga@gmail.com", "sinagarendi")
    private val registerRequest = RegisterRequest("sinaga", "sinaga@gmail.com", "sinagarendi")

    @Before
    fun setUp() {

        loginCase = mock(LoginCase::class.java)
        registerCase = mock(RegisterCase::class.java)
        authViewModel = AuthViewModel(loginCase, registerCase)
    }

    @Test
    fun `User Login Success`() = runTest {
        val expectation = flowOf(
            Sealed.success(DataDummy.loginResponseDummy())
        )

        Mockito.`when`(loginCase.call(loginRequest)).thenReturn(expectation)
        val actual = authViewModel.login(loginRequest).getOrAwaitValue()
        Mockito.verify(loginCase).call(loginRequest)

        advanceUntilIdle()

        Assert.assertNotNull(actual)
        Assert.assertTrue(actual.status == Status.SUCCESS)
        expectation.collect { res ->
            Assert.assertEquals(res.data?.message, actual.data?.message)
        }

    }

    @Test
    fun `User Login Error`() = runTest {
        val expectation = flowOf(
            Sealed.error("User not found", null)
        )

        Mockito.`when`(loginCase.call(loginRequest)).thenReturn(expectation)
        val actual = authViewModel.login(loginRequest).getOrAwaitValue()
        Mockito.verify(loginCase).call(loginRequest)

        advanceUntilIdle()

        Assert.assertNotNull(actual)
        Assert.assertTrue(actual.status == Status.ERROR)
        expectation.collect { res ->
            Assert.assertEquals(res.message, actual.message)
        }
    }

    @Test
    fun `User Register Success`() = runTest {
        val expectation = flowOf(
            Sealed.success(DataDummy.registerResponseDummy())
        )

        Mockito.`when`(registerCase.call(registerRequest)).thenReturn(expectation)
        val actual = authViewModel.register(registerRequest).getOrAwaitValue()
        Mockito.verify(registerCase).call(registerRequest)

        advanceUntilIdle()

        Assert.assertNotNull(actual)
        Assert.assertTrue(actual.status == Status.SUCCESS)
        expectation.collect { res ->
            Assert.assertEquals(res.data, actual.data)
        }
    }

    @Test
    fun `User Register Error`() = runTest {
        val expectation = flowOf(
            Sealed.error("\"email\" must be a valid email", null)
        )

        Mockito.`when`(loginCase.call(loginRequest)).thenReturn(expectation)
        val actual = authViewModel.login(loginRequest).getOrAwaitValue()
        Mockito.verify(loginCase).call(loginRequest)

        advanceUntilIdle()

        Assert.assertNotNull(actual)
        Assert.assertTrue(actual.status == Status.ERROR)
        expectation.collect { res ->
            Assert.assertEquals(res.data, actual.data)
        }
    }
}