package com.example.netweaver.domain.usecase

import com.example.netweaver.domain.repository.AuthRepository
import com.example.netweaver.ui.model.Result
import javax.inject.Inject

class SignInWithEmailUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(
        email: String,
        password: String
    ): Result<Unit> {

        return try {
            when (val result = authRepository.signInWithEmail(email = email, password = password)) {
                is Result.Error -> {
                    Result.Error(result.exception)
                }

                is Result.Success -> {
                    Result.Success(Unit)
                }
            }
        } catch (e: Exception) {
            Result.Error(e)
        }


    }
}