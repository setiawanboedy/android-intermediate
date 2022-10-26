package com.example.ourstory.ui.page.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.ourstory.core.Sealed
import com.example.ourstory.domain.request.LoginRequest
import com.example.ourstory.domain.request.RegisterRequest
import com.example.ourstory.domain.response.LoginResponse
import com.example.ourstory.domain.response.RegisterResponse
import com.example.ourstory.domain.usecase.LoginCase
import com.example.ourstory.domain.usecase.RegisterCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val login: LoginCase,
    private val register: RegisterCase,
) : ViewModel() {
    fun login(req: LoginRequest): LiveData<Sealed<LoginResponse>> = login.call(req)
    fun register(req: RegisterRequest): LiveData<Sealed<RegisterResponse>> = register.call(req)
}