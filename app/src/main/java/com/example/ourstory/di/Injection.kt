package com.example.ourstory.di

import android.content.Context
import com.example.ourstory.BuildConfig.BASE_URL
import com.example.ourstory.BuildConfig.DEBUG
import com.example.ourstory.core.ErrorParser
import com.example.ourstory.core.SafeResponse
import com.example.ourstory.data.datasource.DataSources
import com.example.ourstory.data.local.db.StoryDatabase
import com.example.ourstory.data.network.get.GetService
import com.example.ourstory.data.network.interceptor.InterceptorApi
import com.example.ourstory.data.network.post.AuthService
import com.example.ourstory.data.network.post.PostService
import com.example.ourstory.data.repository.RepositoryImpl
import com.example.ourstory.domain.repository.Repository
import com.example.ourstory.domain.usecase.AddCase
import com.example.ourstory.domain.usecase.LoginCase
import com.example.ourstory.domain.usecase.RegisterCase
import com.example.ourstory.domain.usecase.StoriesCase
import com.example.ourstory.session.Preferences
import com.example.ourstory.session.SessionManager
import com.example.ourstory.ui.view.PopDialog
import com.example.ourstory.utils.Constants
import com.example.ourstory.utils.Constants.BASE_URL_MOCK
import com.example.ourstory.utils.test.TokenTest
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Injection {

    @Provides
    @Singleton
    fun providePopDialog() = PopDialog()

    @Provides
    @Singleton
    fun provideTokenTest() = TokenTest()

    @Provides
    @Singleton
    fun provideSessionManager(@ApplicationContext context: Context) =
        SessionManager(context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE))

    @Provides
    @Singleton
    fun providePreferences(@ApplicationContext context: Context) =
        Preferences(context.getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE))

    @Provides
    @Singleton
    fun provideSafeResponse() = SafeResponse()

    @Provides
    @Singleton
    fun provideOkHttpInterceptor() = HttpLoggingInterceptor().apply {
        level = if (DEBUG) HttpLoggingInterceptor.Level.BODY
        else HttpLoggingInterceptor.Level.NONE
    }

    @Provides
    @Singleton
    fun provideAuthService(retrofit: Retrofit): AuthService =
        retrofit.create(AuthService::class.java)

    @Provides
    @Singleton
    fun providePostService(retrofit: Retrofit): PostService =
        retrofit.create(PostService::class.java)

    @Provides
    @Singleton
    fun provideOkHttpClient(
        interceptor: HttpLoggingInterceptor,
        session: SessionManager,
        tokenTest: TokenTest
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(InterceptorApi(session, tokenTest))
        .addInterceptor(interceptor)
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL_MOCK ?: BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideGetService(retrofit: Retrofit): GetService =
        retrofit.create(GetService::class.java)

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) =
        StoryDatabase.getDatabase(context)

    @Provides
    @Singleton
    fun provideStoryDao(db: StoryDatabase) = db.storyDao()

    @Provides
    @Singleton
    fun provideRemoteDao(db: StoryDatabase) = db.remoteKeyDao()

    @Provides
    @Singleton
    fun provideErrorParser(retrofit: Retrofit) = ErrorParser(retrofit)

    @Provides
    @Singleton
    fun provideDataSource(
        safeResponse: SafeResponse,
        converter: ErrorParser,
        authService: AuthService,
        getService: GetService,
        postService: PostService
    ) = DataSources(safeResponse, converter, authService, getService, postService)

    @Provides
    @Singleton
    fun provideRepository(
        data: DataSources,
        db: StoryDatabase,
        getService: GetService
    ): Repository =
        RepositoryImpl.getInstance(data, db, getService)

    @Provides
    @Singleton
    fun provideRegisterCase(repo: Repository) = RegisterCase(repo)

    @Provides
    @Singleton
    fun provideLoginCase(repo: Repository) = LoginCase(repo)

    @Provides
    @Singleton
    fun provideStoriesCase(repo: Repository) = StoriesCase(repo)

    @Provides
    @Singleton
    fun provideAddCase(repo: Repository) = AddCase(repo)
}