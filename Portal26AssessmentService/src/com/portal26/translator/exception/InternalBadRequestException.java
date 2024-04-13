package com.portal26.translator.exception;

import lombok.Getter;
import lombok.NonNull;

/**
 * Custom Exception for all bad request exceptions.
 */
@Getter
public class InternalBadRequestException extends RuntimeException {

    /**
     * Error code.
     */
    private final String errorCode;

    /**
     * Internal BadRequest exception with message, code and cause.
     *
     * @param message   exception message
     * @param cause     exception cause
     * @param errorCode exception errorCode
     */
    public InternalBadRequestException(@NonNull final String message, @NonNull final Exception cause,
            @NonNull final String errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    /**
     * Internal BadRequest exception with cause and errorCode.
     *
     * @param cause     exception cause
     * @param errorCode exception errorCode
     */
    public InternalBadRequestException(@NonNull final Throwable cause, @NonNull final String errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    /**
     * Internal BadRequest exception with message and errorCode.
     *
     * @param message   exception message
     * @param errorCode exception errorCode
     */
    public InternalBadRequestException(@NonNull final String message, @NonNull final String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Non retryable exception with message and cause.
     *
     * @param message exception message
     * @param cause   exception cause
     */
    public InternalBadRequestException(@NonNull final String message, @NonNull final Exception cause) {
        super(message, cause);
        this.errorCode = cause.getMessage();
    }
}