package mg.andrianina.user.domain.model

data class AuthenticatedUser(
    val user: UserModel,
    val accessToken: String,
    val refreshToken: String,
)
