package mg.andrianina.chirp.api.util

import mg.andrianina.chirp.domain.exception.UnauthorizedException
import mg.andrianina.chirp.domain.type.UserId
import org.springframework.security.core.context.SecurityContextHolder

val requestUserId: UserId
    get() = SecurityContextHolder.getContext().authentication?.principal as? UserId
        ?: throw UnauthorizedException()