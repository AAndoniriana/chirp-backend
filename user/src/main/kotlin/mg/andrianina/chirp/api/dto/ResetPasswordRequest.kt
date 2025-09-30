package mg.andrianina.chirp.api.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import mg.andrianina.chirp.api.util.Password

data class ResetPasswordRequest @JsonCreator constructor(
    @JsonProperty("token")
    @field:NotBlank
    val token: String,
    @JsonProperty("newPassword")
    @field:Password
    val newPassword: String
)
