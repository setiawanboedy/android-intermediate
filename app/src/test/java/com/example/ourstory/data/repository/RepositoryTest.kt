package com.example.ourstory.data.repository

import com.example.ourstory.data.datasource.DataSources
import com.example.ourstory.utils.CoroutinesTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RepositoryTest {
    @get:Rule
    var coroutineTestRule = CoroutinesTestRule()

    @Mock
    private lateinit var source: DataSources

    @Mock
    private lateinit var sourcesMock: DataSources

    @Mock
    private lateinit var repoMock: RepositoryImpl


}