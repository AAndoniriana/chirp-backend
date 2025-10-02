package mg.andrianina.chirp.domain.exceptions

class RateLimitException(
    val resetsInSeconds: Long,
): RuntimeException("Rate limit exceeded. Please try again in $resetsInSeconds seconds.")