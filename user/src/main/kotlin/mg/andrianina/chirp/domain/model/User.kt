package mg.andrianina.chirp.domain.model

import mg.andrianina.chirp.domain.type.UserId

data class User(
    val id: UserId,
    val username: String,
    val email: String,
    val hasEmailVerified: Boolean,
)
