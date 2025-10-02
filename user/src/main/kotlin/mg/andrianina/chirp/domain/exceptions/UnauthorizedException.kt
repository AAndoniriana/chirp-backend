package mg.andrianina.chirp.domain.exceptions

import java.lang.RuntimeException

class UnauthorizedException: RuntimeException("Missing auth details")