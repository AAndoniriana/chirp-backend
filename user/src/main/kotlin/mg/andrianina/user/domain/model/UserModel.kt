package mg.andrianina.user.domain.model

import java.util.UUID

typealias UserId = UUID

data class UserModel(
    val id: UserId,
    val username: String,
    val email: String,
    val haEmailVerified: Boolean,
)
