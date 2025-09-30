package mg.andrianina.chirp.api.controllers

import jakarta.validation.Valid
import mg.andrianina.chirp.api.dto.AuthenticatedUserDto
import mg.andrianina.chirp.api.dto.ChangePasswordRequest
import mg.andrianina.chirp.api.dto.EmailRequest
import mg.andrianina.chirp.api.dto.LoginRequest
import mg.andrianina.chirp.api.dto.RefreshRequest
import mg.andrianina.chirp.api.dto.RegisterRequest
import mg.andrianina.chirp.api.dto.ResetPasswordRequest
import mg.andrianina.chirp.api.dto.UserDto
import mg.andrianina.chirp.api.mappers.toDto
import mg.andrianina.chirp.service.AuthService
import mg.andrianina.chirp.service.EmailVerificationService
import mg.andrianina.chirp.service.PasswordResetService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService,
    private val emailVerificationService: EmailVerificationService,
    private val passwordResetService: PasswordResetService
) {

    @PostMapping("/register")
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
    fun login(
        @RequestBody body: LoginRequest
    ): AuthenticatedUserDto {
        return authService.login(
            email = body.email,
            password = body.password
        ).toDto()
    }

    @PostMapping("/refresh")
    fun refresh(
        @RequestBody body: RefreshRequest
    ): AuthenticatedUserDto {
        return authService.refreshToken(body.refreshToken).toDto()
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
        // TODO: extract userId from jwt
        /*passwordResetService.changePassword(
            userId = ,
            oldPassword = body.oldPassword,
            newPassword = body.newPassword
        )*/
    }
}