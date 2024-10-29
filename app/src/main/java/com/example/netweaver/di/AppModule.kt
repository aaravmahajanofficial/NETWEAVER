package com.example.netweaver.di

import com.example.netweaver.data.repository.RepositoryImplementation
import com.example.netweaver.domain.repository.Repository
import com.example.netweaver.domain.usecase.GetPostsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.postgrest.Postgrest
import javax.inject.Singleton

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

}