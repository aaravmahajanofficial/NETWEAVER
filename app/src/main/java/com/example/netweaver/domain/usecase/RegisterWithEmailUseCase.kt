package com.example.netweaver.domain.usecase

import com.example.netweaver.domain.repository.AuthRepository
import com.example.netweaver.ui.model.Result
import javax.inject.Inject

class RegisterWithEmailUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(
        email: String,
        password: String,
        firstName: String,
        lastName: String
    ): Result<Unit> {

        return try {

            when (val result =
                authRepository.registerWithEmail(
                    email = email,
                    password = password,
                    firstName = firstName,
                    lastName = lastName
                )) {
                is Result.Success -> {
                    Result.Success(Unit)
                }

                is Result.Error -> {
                    Result.Error(result.exception)
                }
            }

        } catch (e: Exception) {
            Result.Error(e)
        }

    }
}