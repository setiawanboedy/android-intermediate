package com.example.ourstory.ui.page.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.ourstory.core.Sealed
import com.example.ourstory.request.AddRequest
import com.example.ourstory.response.GenericResponse
import com.example.ourstory.usecase.AddCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(
    private val add: AddCase
) : ViewModel() {
    fun addStory(req: AddRequest): LiveData<Sealed<GenericResponse>> = add.call(req)
}