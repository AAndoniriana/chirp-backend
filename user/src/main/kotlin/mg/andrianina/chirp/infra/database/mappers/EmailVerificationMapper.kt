package mg.andrianina.chirp.infra.database.mappers

import mg.andrianina.chirp.domain.model.EmailVerificationToken
import mg.andrianina.chirp.infra.database.entity.EmailVerificationTokenEntity

fun EmailVerificationTokenEntity.toEmailVerification(): EmailVerificationToken {
    return EmailVerificationToken(
        id = id,
        token = token,
        user = user.toUser(),
    )
}