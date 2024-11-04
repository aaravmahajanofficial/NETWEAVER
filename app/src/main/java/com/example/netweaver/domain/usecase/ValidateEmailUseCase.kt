package com.example.netweaver.domain.usecase

import android.util.Patterns

class ValidateEmailUseCase {

    operator fun invoke(email: String): String? {

        if (email.isBlank()) return "Email cannot be blank"
        if (!Patterns.EMAIL_ADDRESS.matcher(email)
                .matches()
        ) return "Invalid email format"

        return null
    }

}