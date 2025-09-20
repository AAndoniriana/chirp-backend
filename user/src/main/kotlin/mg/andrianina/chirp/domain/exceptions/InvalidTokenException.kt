package mg.andrianina.chirp.domain.exceptions

class InvalidTokenException(
    override val message: String?
): RuntimeException(
    message ?: "Invalid token"
)