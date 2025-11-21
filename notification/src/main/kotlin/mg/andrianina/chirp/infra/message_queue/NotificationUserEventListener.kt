package mg.andrianina.chirp.infra.message_queue

import mg.andrianina.chirp.domain.events.user.UserEvent
import mg.andrianina.chirp.service.EmailService
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class NotificationUserEventListener(private val emailService: EmailService) {
    @RabbitListener(queues = [MessageQueue.NOTIFICATION_USER_EVENT])
    fun handleUserEvent(event: UserEvent) {
        when (event) {
            is UserEvent.Created -> {
                emailService.sendVerificationEmail(
                    email = event.email,
                    username = event.username,
                    userId = event.userId,
                    token = event.verificationToken
                )
            }
            is UserEvent.RequestResendVerification -> {
                emailService.sendVerificationEmail(
                    email = event.email,
                    username = event.username,
                    userId = event.userId,
                    token = event.verificationToken
                )
            }
            is UserEvent.RequestResetPassword -> {
                emailService.sendPasswordResetEmail(
                    email = event.email,
                    username = event.username,
                    userId = event.userId,
                    token = event.verificationToken,
                    expiresIn = Duration.ofMinutes(event.expiresInMinutes)
                )
            }
            is UserEvent.Verified -> {
                println("User verified")
            }
        }
    }
}