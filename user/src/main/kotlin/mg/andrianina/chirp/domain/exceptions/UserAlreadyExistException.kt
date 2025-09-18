package mg.andrianina.chirp.domain.exceptions

import java.lang.RuntimeException

class UserAlreadyExistException : RuntimeException("A user with this username or email already exists")