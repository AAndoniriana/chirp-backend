package mg.andrianina.chirp.exception

class InvalidChatSizeException: RuntimeException(
    "There must be at least two unique member."
)