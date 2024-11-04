package com.example.netweaver.domain.usecase

import android.util.Log
import com.example.netweaver.domain.repository.AuthRepository
import com.example.netweaver.ui.model.Result
import javax.inject.Inject

class SignInWithEmailUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(
        email: String,
        password: String
    ): Result<String> {

        return try {
            when (val result = authRepository.signInWithEmail(email = email, password = password)) {
                is Result.Error -> {
                    Log.d("FIREBASE AUTH", result.exception.message.toString())
                    Result.Error(result.exception)
                }

                is Result.Success -> {
                    Result.Success(result.data?.uid.toString())
                }
            }
        } catch (e: Exception) {
            Log.d("FIREBASE AUTH FROM CATCH", e.message.toString())
            Result.Error(e)
        }


    }
}