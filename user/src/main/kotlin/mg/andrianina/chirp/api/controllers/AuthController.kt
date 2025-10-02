package mg.andrianina.chirp.api.controllers

import jakarta.validation.Valid
import mg.andrianina.chirp.api.config.IpRateLimit
import mg.andrianina.chirp.api.dto.AuthenticatedUserDto
import mg.andrianina.chirp.api.dto.ChangePasswordRequest
import mg.andrianina.chirp.api.dto.EmailRequest
import mg.andrianina.chirp.api.dto.LoginRequest
import mg.andrianina.chirp.api.dto.RefreshRequest
import mg.andrianina.chirp.api.dto.RegisterRequest
import mg.andrianina.chirp.api.dto.ResetPasswordRequest
import mg.andrianina.chirp.api.dto.UserDto
import mg.andrianina.chirp.api.mappers.toDto
import mg.andrianina.chirp.api.util.requestUserId
import mg.andrianina.chirp.infra.rate_limiting.EmailRateLimiter
import mg.andrianina.chirp.service.AuthService
import mg.andrianina.chirp.service.EmailVerificationService
import mg.andrianina.chirp.service.PasswordResetService
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.TimeUnit

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService,
    private val emailVerificationService: EmailVerificationService,
    private val passwordResetService: PasswordResetService,
    private val emailRateLimiter: EmailRateLimiter
) {

    @PostMapping("/register")
    @IpRateLimit(
        requests = 10,
        duration = 1L,
        timeUnit = TimeUnit.HOURS
    )
    fun register(
        @Valid @RequestBody body: RegisterRequest
    ): UserDto {
        return authService.register(
            email = body.email,
            password = body.password,
            username = body.username,
        ).toDto()
    }

    @PostMapping("/login")
    @IpRateLimit(
        requests = 10,
        duration = 1L,
        timeUnit = TimeUnit.HOURS
    )
    fun login(
        @RequestBody body: LoginRequest
    ): AuthenticatedUserDto {
        return authService.login(
            email = body.email,
            password = body.password
        ).toDto()
    }

    @PostMapping("/refresh")
    @IpRateLimit(
        requests = 10,
        duration = 1L,
        timeUnit = TimeUnit.HOURS
    )
    fun refresh(
        @RequestBody body: RefreshRequest
    ): AuthenticatedUserDto {
        return authService.refreshToken(body.refreshToken).toDto()
    }

    @PostMapping("/logout")
    fun logout(
        @RequestBody body: RefreshRequest
    ) {
        authService.logout(body.refreshToken)
    }

    @PostMapping("/resend-verification")
    @IpRateLimit(
        requests = 10,
        duration = 1L,
        timeUnit = TimeUnit.HOURS
    )
    fun resendVerification(
        @Valid @RequestBody body: EmailRequest
    ) {
        emailRateLimiter.withRateLimit(
            email = body.email
        ) {
            emailVerificationService.resendVerificationEmail(body.email)
        }
    }

    @GetMapping("/verify")
    fun verifyEmail(
        @RequestParam token: String,
    ) {
        emailVerificationService.verifyEmail(token)
    }

    @PostMapping("/reset-password")
    fun resetPassword(
        @RequestBody @Valid body: ResetPasswordRequest
    ) {
        passwordResetService.resetPassword(
            token = body.token,
            newPassword = body.newPassword
        )
    }

    @PostMapping("/forgot-password")
    @IpRateLimit(
        requests = 10,
        duration = 1L,
        timeUnit = TimeUnit.HOURS
    )
    fun forgotPassword(
        @RequestBody @Valid body: EmailRequest
    ) {
        passwordResetService.requestPasswordReset(
            email = body.email,
        )
    }

    @PostMapping("/change-password")
    fun changePassword(
        @RequestBody @Valid body: ChangePasswordRequest
    ) {
        passwordResetService.changePassword(
            userId = requestUserId,
            oldPassword = body.oldPassword,
            newPassword = body.newPassword
        )
    }
}