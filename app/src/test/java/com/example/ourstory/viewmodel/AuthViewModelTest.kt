package com.example.ourstory.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.ourstory.core.Sealed
import com.example.ourstory.domain.request.LoginRequest
import com.example.ourstory.domain.request.RegisterRequest
import com.example.ourstory.domain.response.LoginResponse
import com.example.ourstory.domain.response.RegisterResponse
import com.example.ourstory.ui.page.auth.AuthViewModel
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

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AuthViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesRuleTest = CoroutinesRuleTest()

    @Mock
    lateinit var viewModel: AuthViewModel

    private val loginResponse = DataDummy.loginResponseDummy()

    private val loginRequest = LoginRequest("sinaga@gmail.com", "sinagarendi")
    private val registerRequest = RegisterRequest("sinaga", "sinaga@gmail.com", "sinagarendi")

    @Test
    fun `User Login Success`() = runTest {
        val expectation = loginResponse
        val login = MutableLiveData<Sealed<LoginResponse>>()
        login.value = Sealed.success(expectation)

        Mockito.`when`(viewModel.login(loginRequest)).thenReturn(login)
        val actual = viewModel.login(loginRequest).getOrAwaitValue()
        Mockito.verify(viewModel).login(loginRequest)

        advanceUntilIdle()

        Assert.assertNotNull(actual)
        Assert.assertEquals(actual.data, expectation)
    }

    @Test
    fun `User Login Error`() = runTest {
        val login = MutableLiveData<Sealed<LoginResponse>>()
        login.value = Sealed.error("UNKNOWN ERROR", null)

        Mockito.`when`(viewModel.login(loginRequest)).thenReturn(login)
        val actual = viewModel.login(loginRequest).getOrAwaitValue()
        Mockito.verify(viewModel).login(loginRequest)

        advanceUntilIdle()

        Assert.assertNotNull(actual)
        Assert.assertEquals(login.value, actual)
    }

    @Test
    fun `User Register Success`() = runTest {
        val expectation = DataDummy.registerResponseDummy()
        val register = MutableLiveData<Sealed<RegisterResponse>>()
        register.value = Sealed.success(expectation)

        Mockito.`when`(viewModel.register(registerRequest)).thenReturn(register)
        val actual = viewModel.register(registerRequest).getOrAwaitValue()
        Mockito.verify(viewModel).register(registerRequest)

        advanceUntilIdle()

        Assert.assertNotNull(actual)
        Assert.assertEquals(actual.data, expectation)
    }

    @Test
    fun `User Register Error`() = runTest {

        val register = MutableLiveData<Sealed<RegisterResponse>>()
        register.value = Sealed.error("UNKNOWN ERROR", null)

        Mockito.`when`(viewModel.register(registerRequest)).thenReturn(register)
        val actual = viewModel.register(registerRequest).getOrAwaitValue()
        Mockito.verify(viewModel).register(registerRequest)

        advanceUntilIdle()

        Assert.assertNotNull(actual)
        Assert.assertEquals(register.value, actual)
    }
}