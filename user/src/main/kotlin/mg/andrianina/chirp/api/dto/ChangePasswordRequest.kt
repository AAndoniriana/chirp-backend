package mg.andrianina.chirp.api.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import mg.andrianina.chirp.api.util.Password

data class ChangePasswordRequest @JsonCreator constructor(
    @JsonProperty("oldPassword")
    @field:NotBlank
    val oldPassword: String,
    @JsonProperty("newPassword")
    @field:Password
    val newPassword: String
)
