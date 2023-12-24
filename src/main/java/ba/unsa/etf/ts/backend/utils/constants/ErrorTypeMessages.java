package ba.unsa.etf.ts.backend.utils.constants;

public enum ErrorTypeMessages {
    RESOURCE_NOT_FOUND("Resource not found"),
    UNAUTHORIZED("Unauthorized access"),
    BAD_REQUEST("Bad request"),
    METHOD_ARGUMENT_INVALID("Validation error"),
    AUTHENTICATION_ERROR("Bad credentials");





    private final String errorTypeMessage;

    ErrorTypeMessages(final String errorTypeMessage){
        this.errorTypeMessage = errorTypeMessage;
    }
    @Override
    public String toString() {
        return errorTypeMessage;
    }
}
