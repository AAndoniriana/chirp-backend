package mg.andrianina.chirp.api.controllers

import jakarta.validation.Valid
import mg.andrianina.chirp.api.dto.AuthenticatedUserDto
import mg.andrianina.chirp.api.dto.LoginRequest
import mg.andrianina.chirp.api.dto.RegisterRequest
import mg.andrianina.chirp.api.dto.UserDto
import mg.andrianina.chirp.api.mappers.toDto
import mg.andrianina.chirp.service.auth.AuthService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(private val authService: AuthService) {

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

}