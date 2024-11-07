package com.example.netweaver.domain.usecase.validation

class ValidateNameUseCase {

    operator fun invoke(name: String): String? {

        val isValidLength = name.length in 1..50
        val nameRegex = "^[A-Za-zÀ-ÖØ-öø-ÿ'\\-\\s]+$".toRegex()

        return when {
            name.isBlank() -> "Name cannot be empty."
            !isValidLength -> "Name must be between 1 and 50 characters."
            !name.matches(nameRegex) -> "Names can only letters, spaces, hyphens, and apostrophes."
            else -> {
                null
            }
        }

    }

}