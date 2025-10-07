package mg.andrianina.chirp.infra.message_queue

import mg.andrianina.chirp.domain.events.user.UserEvent
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class NotificationUserEventListener {
    @RabbitListener(queues = [MessageQueue.NOTIFICATION_USER_EVENT])
    fun handleUserEvent(event: UserEvent) {
        when (event) {
            is UserEvent.Created -> {
                println("User created")
            }
            is UserEvent.RequestResendVerification -> {
                println("Request resend verification")
            }
            is UserEvent.RequestResetPassword -> {
                println("Request reset password")
            }
            is UserEvent.Verified -> {
                println("User verified")
            }
        }
    }
}