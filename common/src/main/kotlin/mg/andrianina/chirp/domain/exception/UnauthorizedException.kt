package mg.andrianina.chirp.domain.exception

import java.lang.RuntimeException

class UnauthorizedException: RuntimeException("Missing auth details")