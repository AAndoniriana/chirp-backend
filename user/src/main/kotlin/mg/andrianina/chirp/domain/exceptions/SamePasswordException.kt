package mg.andrianina.chirp.domain.exceptions

class SamePasswordException: RuntimeException(
    "The new password can't be equal to the old one."
)