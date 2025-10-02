package mg.andrianina.chirp.api.config

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import mg.andrianina.chirp.domain.exceptions.RateLimitException
import mg.andrianina.chirp.infra.rate_limiting.IpRateLimiter
import mg.andrianina.chirp.infra.rate_limiting.IpResolver
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import java.time.Duration

@Component
class IpRateLimitInterceptor(
    private val ipRateLimiter: IpRateLimiter,
    private val ipResolver: IpResolver,
    @param:Value($$"${chirp.rate-limit.ip.apply-limit}")
    private val applyRateLimit: Boolean
): HandlerInterceptor {
    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any
    ): Boolean {
        if (handler is HandlerMethod && applyRateLimit) {
            val annotation = handler.getMethodAnnotation(IpRateLimit::class.java)
            if (annotation != null) {
                val clientIp = ipResolver.getClientIp(request)

                return try {
                    ipRateLimiter.withIpRateLimit(
                        ipAddress = clientIp,
                        resetsIn = Duration.of(
                            annotation.duration,
                            annotation.timeUnit.toChronoUnit()
                        ),
                        maxRequestPerIp = annotation.requests,
                        action = { true },
                    )
                } catch (e: RateLimitException) {
                    response.sendError(429)
                    false
                }
            }
        }

        return super.preHandle(request, response, handler)
    }
}