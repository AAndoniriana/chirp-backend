package mg.andrianina.chirp.api.dto

import jakarta.validation.constraints.NotBlank
import mg.andrianina.chirp.api.util.Password

data class ResetPasswordRequest(
    @field:NotBlank
    val token: String,
    @field:Password
    val newPassword: String
)
