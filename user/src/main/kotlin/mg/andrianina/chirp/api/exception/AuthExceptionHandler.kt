package mg.andrianina.chirp.api.exception

import mg.andrianina.chirp.domain.exceptions.InvalidTokenException
import mg.andrianina.chirp.domain.exceptions.UserAlreadyExistException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class AuthExceptionHandler {

    @ExceptionHandler(UserAlreadyExistException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun onUserAlreadyExistException(
        e: UserAlreadyExistException
    ) = mapOf(
        "code" to "USER_EXIST",
        "message" to e.message
    )

    @ExceptionHandler(InvalidTokenException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun onInvalidTokenException(
        e: InvalidTokenException
    ) = mapOf(
        "code" to "INVALID_TOKEN",
        "message" to e.message
    )

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun onValidationException(e: MethodArgumentNotValidException): ResponseEntity<Map<String, Any>> {
        val errors = e.bindingResult.allErrors.map {
            it.defaultMessage ?: "Invalid Value"
        }

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                mapOf(
                    "code" to "VALIDATION_ERROR",
                    "errors" to errors
                )
            )
    }

}