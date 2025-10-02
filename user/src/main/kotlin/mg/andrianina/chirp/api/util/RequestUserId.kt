package mg.andrianina.chirp.api.util

import mg.andrianina.chirp.domain.exceptions.UnauthorizedException
import mg.andrianina.chirp.domain.model.UserId
import org.springframework.security.core.context.SecurityContextHolder

val requestUserId: UserId
    get() = SecurityContextHolder.getContext().authentication?.principal as? UserId
        ?: throw UnauthorizedException()