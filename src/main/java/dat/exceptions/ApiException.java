package dat.exceptions;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public class ApiException extends RuntimeException {
    private static final Logger logger = LoggerFactory.getLogger(ApiException.class);
    private final int statusCode;

    public ApiException(int statusCode, String errorMessage) {
        super(errorMessage);
        this.statusCode = statusCode;
        writeToLog(errorMessage);
    }

    private void writeToLog(String errorMessage) {
        logger.error("Error occurred: Status Code: {}, Message: {}", statusCode, errorMessage);
    }
}
