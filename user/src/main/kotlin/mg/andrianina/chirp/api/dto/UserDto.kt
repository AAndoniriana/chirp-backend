package mg.andrianina.chirp.api.dto

import mg.andrianina.chirp.domain.type.UserId

data class UserDto(
    val id: UserId,
    val username: String,
    val email: String,
    val hasVerifiedEmail: Boolean
)
