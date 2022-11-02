package com.example.ourstory.domain.usecase

import com.example.ourstory.core.Sealed
import kotlinx.coroutines.flow.Flow

interface UseCase<T, R> {
    fun call(req: R): Flow<Sealed<T>>
}