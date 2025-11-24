package mg.andrianina.chirp.api.dto

import jakarta.validation.constraints.NotBlank
import mg.andrianina.chirp.api.util.Password

data class ChangePasswordRequest(
    @field:NotBlank
    val oldPassword: String,
    @field:Password
    val newPassword: String
)
