package mg.andrianina.chirp.api.controllers

import jakarta.validation.Valid
import mg.andrianina.chirp.api.dto.AuthenticatedUserDto
import mg.andrianina.chirp.api.dto.LoginRequest
import mg.andrianina.chirp.api.dto.RefreshRequest
import mg.andrianina.chirp.api.dto.RegisterRequest
import mg.andrianina.chirp.api.dto.UserDto
import mg.andrianina.chirp.api.mappers.toDto
import mg.andrianina.chirp.service.auth.AuthService
import mg.andrianina.chirp.service.auth.EmailVerificationService
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
    private val emailVerificationService: EmailVerificationService
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
}