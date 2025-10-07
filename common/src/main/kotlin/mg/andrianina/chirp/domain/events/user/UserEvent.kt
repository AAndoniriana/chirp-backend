package mg.andrianina.chirp.domain.events.user

import mg.andrianina.chirp.domain.events.ChirpEvent
import mg.andrianina.chirp.domain.type.UserId
import java.time.Instant
import java.util.UUID

sealed class UserEvent(
    override val id: String = UUID.randomUUID().toString(),
    override val exchange: String = UserEventConstants.USER_EXCHANGE,
    override val occurredAt: Instant = Instant.now(),
): ChirpEvent {

    data class Created(
        val userId: UserId,
        val email: String,
        val username: String,
        val verificationToken: String,
        override val key: String = UserEventConstants.USER_CREATED_KEY,
    ): UserEvent(), ChirpEvent

    data class Verified(
        val userId: UserId,
        val email: String,
        val username: String,
        override val key: String = UserEventConstants.USER_VERIFIED_EVENT,
    ): UserEvent(), ChirpEvent

    data class RequestResendVerification(
        val userId: UserId,
        val email: String,
        val username: String,
        val verificationToken: String,
        override val key: String = UserEventConstants.USER_REQUEST_RESEND_VERIFICATION,
    ): UserEvent(), ChirpEvent

    data class RequestResetPassword(
        val userId: UserId,
        val email: String,
        val username: String,
        val verificationToken: String,
        val expiresInMinutes: Long,
        override val key: String = UserEventConstants.USER_REQUEST_RESET_PASSWORD,
    ): UserEvent(), ChirpEvent
}