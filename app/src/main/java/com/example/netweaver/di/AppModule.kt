package com.example.netweaver.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.netweaver.data.repository.AuthRepositoryImplementation
import com.example.netweaver.data.repository.RepositoryImplementation
import com.example.netweaver.domain.repository.AuthRepository
import com.example.netweaver.domain.repository.Repository
import com.example.netweaver.domain.usecase.CreatePostUseCase
import com.example.netweaver.domain.usecase.GetPostsUseCase
import com.example.netweaver.domain.usecase.SignInWithEmailUseCase
import com.example.netweaver.domain.usecase.ValidateEmailUseCase
import com.example.netweaver.domain.usecase.ValidatePasswordUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import javax.inject.Singleton

private val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRepository(
        postgrest: Postgrest,
        supabaseStorage: Storage,
        firestore: FirebaseFirestore
    ): Repository =
        RepositoryImplementation(
            postgrest = postgrest,
            firestore = firestore,
            supabaseStorage = supabaseStorage
        )

    @Provides
    @Singleton
    fun provideFireStore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideGetPostsUseCase(
        repository: Repository
    ): GetPostsUseCase =
        GetPostsUseCase(repository = repository)

//    @Provides
//    @Singleton
//    fun provideUserPreferencesRepository(@ApplicationContext context: Context): UserPreferencesRepository =
//        UserPreferencesRepository(context.datastore)

    @Provides
    @Singleton

    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideAuthRepository(firebaseAuth: FirebaseAuth, repository: Repository): AuthRepository =
        AuthRepositoryImplementation(firebaseAuth = firebaseAuth, repository = repository)


    @Provides
    @Singleton
    fun provideCreatePostUseCase(repository: Repository) =
        CreatePostUseCase(repository = repository)

    @Provides
    @Singleton
    fun provideValidateEmailUseCase() = ValidateEmailUseCase()

    @Provides
    @Singleton
    fun provideValidatePasswordUseCase() = ValidatePasswordUseCase()

    @Provides
    @Singleton
    fun provideSignInWithEmailUseCase(authRepository: AuthRepository) =
        SignInWithEmailUseCase(authRepository = authRepository)
}
