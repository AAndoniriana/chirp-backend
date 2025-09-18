package mg.andrianina.chirp.api.mappers

import mg.andrianina.chirp.api.dto.AuthenticatedUserDto
import mg.andrianina.chirp.api.dto.UserDto
import mg.andrianina.chirp.domain.model.AuthenticatedUser
import mg.andrianina.chirp.domain.model.User

fun AuthenticatedUser.toDto(): AuthenticatedUserDto = AuthenticatedUserDto(
    user = user.toDto(),
    accessToken = accessToken,
    refreshToken = refreshToken
)

fun User.toDto(): UserDto = UserDto(
    id = id,
    username = username,
    email = email,
    hasVerifiedEmail = hasEmailVerified,
)