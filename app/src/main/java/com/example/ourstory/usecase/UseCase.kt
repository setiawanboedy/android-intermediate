package com.example.ourstory.usecase

import androidx.lifecycle.LiveData
import com.example.ourstory.core.Sealed

interface UseCase<T, R> {
    fun call(req: R): LiveData<Sealed<T>>
}