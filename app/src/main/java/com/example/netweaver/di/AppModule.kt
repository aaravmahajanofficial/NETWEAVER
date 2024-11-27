package com.example.netweaver.di

import com.example.netweaver.data.repository.AuthRepositoryImplementation
import com.example.netweaver.data.repository.RepositoryImplementation
import com.example.netweaver.domain.repository.AuthRepository
import com.example.netweaver.domain.repository.Repository
import com.example.netweaver.domain.usecase.validation.ValidateEmailUseCase
import com.example.netweaver.domain.usecase.validation.ValidateNameUseCase
import com.example.netweaver.domain.usecase.validation.ValidatePasswordUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import javax.inject.Singleton

//private val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRepository(
        postgrest: Postgrest,
        supabaseStorage: Storage,
        firebaseAuth: FirebaseAuth,
        supabaseClient: SupabaseClient
    ): Repository =
        RepositoryImplementation(
            postgrest = postgrest,
            supabaseStorage = supabaseStorage,
            firebaseAuth = firebaseAuth,
            supabaseClient = supabaseClient
        )

    @Provides
    @Singleton

    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideAuthRepository(firebaseAuth: FirebaseAuth, repository: Repository): AuthRepository =
        AuthRepositoryImplementation(firebaseAuth = firebaseAuth, repository = repository)

    @Provides
    @Singleton
    fun provideValidateEmailUseCase() = ValidateEmailUseCase()

    @Provides
    @Singleton
    fun provideValidateNameUseCase() = ValidateNameUseCase()

    @Provides
    @Singleton
    fun provideValidatePasswordUseCase() = ValidatePasswordUseCase()

}
