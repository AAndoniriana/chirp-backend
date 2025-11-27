package mg.andrianina.chirp.exception

import mg.andrianina.chirp.domain.type.UserId

class ChatParticipantNotFoundException(
    id: UserId
): RuntimeException(
    "The chat participant with id $id was not found"
)