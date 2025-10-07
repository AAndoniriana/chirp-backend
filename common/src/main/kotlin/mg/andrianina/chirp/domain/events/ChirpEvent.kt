package mg.andrianina.chirp.domain.events

import java.time.Instant

interface ChirpEvent {
    val id: String
    val key: String
    val occurredAt: Instant
    val exchange: String
}