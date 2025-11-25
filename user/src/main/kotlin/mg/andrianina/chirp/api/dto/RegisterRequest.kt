package mg.andrianina.chirp.api.dto

import jakarta.validation.constraints.Email
import mg.andrianina.chirp.api.util.Password
import org.hibernate.validator.constraints.Length

data class RegisterRequest(
    @field:Email(message = "Must be a valid email address")
    val email: String,
    val username: String,
    @field:Length(min = 3, max = 20, message = "Username length must be between 3 and 20 characters")
    @field:Password
    val password: String
)
