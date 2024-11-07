package com.example.netweaver.domain.usecase.validation

class ValidatePasswordUseCase {

    operator fun invoke(password: String): String? {

        return when {
            password.length < 8 -> "Password must be at least 8 characters long."
            !password.contains(Regex("[0-9]")) -> "Password must contain at least one digit."
            !password.contains(Regex("[a-z]")) -> "Password must contain at least one lowercase letter."
            !password.contains(Regex("[A-Z]")) -> "Password must contain at least one uppercase letter."
            !password.contains(Regex("[@#$%^&+=]")) -> "Password must contain at least one special character."
            password.contains(Regex("\\s")) -> "Password must not contain any whitespace."
            else -> null
        }

    }
}