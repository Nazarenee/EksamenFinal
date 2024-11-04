package dat.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import dat.constants.Message;
import io.javalin.Javalin;
import io.javalin.http.Context;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.RollbackException;
import io.javalin.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExceptionController {
    private final Logger logger = LoggerFactory.getLogger(ExceptionController.class);

    public void setExceptionHandlers(Javalin app) {
        app.exception(EntityNotFoundException.class, this::entityNotFoundExceptionHandler);
        app.exception(RollbackException.class, this::rollbackExceptionHandler);
        app.exception(JsonProcessingException.class, this::jsonProcessingExceptionHandler);
        app.exception(Exception.class, this::generalExceptionHandler);
        app.exception(NumberFormatException.class, this::numberFormatExceptionHandler);
        app.exception(EntityExistsException.class, this::entityExistsExceptionHandler);
    }

    /**
     * This method is called when an entity that doesn't exist within the database is requested.
     */
    public void entityNotFoundExceptionHandler(EntityNotFoundException e, Context ctx) {
        logger.warn("Entity not found: {}", e.getMessage());
        ctx.status(HttpStatus.NOT_FOUND);
        ctx.json(new Message(HttpStatus.NOT_FOUND.getCode(), e.getMessage()));
    }

    /**
     * This method is called when a transaction has failed and is being rolled back.
     */
    public void rollbackExceptionHandler(RollbackException e, Context ctx) {
        logger.warn("Transaction rolled back: {}", e.getMessage());
        ctx.status(HttpStatus.INTERNAL_SERVER_ERROR);
        ctx.json(new Message(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), "Transaction failed."));
    }

    /**
     * This method is called when the request body is not a valid JSON object.
     */
    public void jsonProcessingExceptionHandler(JsonProcessingException e, Context ctx) {
        logger.warn("JSON processing error: {}", e.getMessage());
        ctx.status(HttpStatus.BAD_REQUEST);
        ctx.json(new Message(HttpStatus.BAD_REQUEST.getCode(), "Malformed JSON in request body."));
    }

    /**
     * This method is called when an unhandled exception occurs.
     */
    public void generalExceptionHandler(Exception e, Context ctx) {
        logger.error("{} {} {}", ctx.attribute("requestInfo"), ctx.res().getStatus(), e.getMessage());
        ctx.status(HttpStatus.INTERNAL_SERVER_ERROR);
        ctx.json(new Message(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), "Internal server error."));
    }

    public void numberFormatExceptionHandler(NumberFormatException e, Context ctx) {
        logger.warn("Number format error: {}", e.getMessage());
        ctx.status(HttpStatus.BAD_REQUEST);
        ctx.json(new Message(HttpStatus.BAD_REQUEST.getCode(), "Invalid parameter given: id."));
    }

    public void entityExistsExceptionHandler(EntityExistsException e, Context ctx) {
        logger.warn("Entity already exists: {}", e.getMessage());
        ctx.status(HttpStatus.CONFLICT);
        ctx.json(new Message(HttpStatus.CONFLICT.getCode(), "Resource already exists."));
    }


}
