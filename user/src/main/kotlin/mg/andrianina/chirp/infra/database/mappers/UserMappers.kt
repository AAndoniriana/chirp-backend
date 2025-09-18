package mg.andrianina.chirp.infra.database.mappers

import mg.andrianina.chirp.domain.model.User
import mg.andrianina.chirp.infra.database.entity.UserEntity

fun UserEntity.toUser() = User(
    id = id!!,
    username = username,
    email = email,
    hasEmailVerified = hasVerifiedEmail,
)