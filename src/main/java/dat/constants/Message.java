package dat.constants;

/**
 * ErrorMessage is used to wrap and present an error message to the client.
 *
 * @param status  the HTTP status code of the error.
 * @param message the error message being presented to the client.
 */
public record Message(int status, String message) {
}

