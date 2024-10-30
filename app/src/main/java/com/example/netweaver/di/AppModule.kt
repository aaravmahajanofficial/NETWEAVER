package com.example.netweaver.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.netweaver.data.local.datastore.UserPreferencesRepository
import com.example.netweaver.data.repository.AuthRepositoryImplementation
import com.example.netweaver.data.repository.RepositoryImplementation
import com.example.netweaver.domain.repository.AuthRepository
import com.example.netweaver.domain.repository.Repository
import com.example.netweaver.domain.usecase.GetPostsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.postgrest.Postgrest
import javax.inject.Singleton

private val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRepository(
        postgrest: Postgrest
    ): Repository =
        RepositoryImplementation(postgrest = postgrest)

    @Provides
    @Singleton
    fun provideGetPostsUseCase(
        repository: Repository
    ): GetPostsUseCase =
        GetPostsUseCase(repository = repository)

    @Provides
    @Singleton
    fun provideDataStore(context: Context): DataStore<Preferences> =
        context.datastore

    @Provides
    @Singleton
    fun provideUserPreferencesRepository(datastore: DataStore<Preferences>): UserPreferencesRepository =
        UserPreferencesRepository(datastore = datastore)

    @Provides
    @Singleton
    fun provideAuthRepository(userPreferencesRepository: UserPreferencesRepository): AuthRepository =
        AuthRepositoryImplementation(userPreferencesRepository = userPreferencesRepository)
}