package com.example.netweaver.domain.usecase.validation

import android.util.Patterns

class ValidateEmailUseCase {

    operator fun invoke(email: String): String? {

        if (email.isBlank()) return "Please enter your email address or phone number"
        if (!Patterns.EMAIL_ADDRESS.matcher(email)
                .matches()
        ) return "Invalid email format"

        return null
    }

}