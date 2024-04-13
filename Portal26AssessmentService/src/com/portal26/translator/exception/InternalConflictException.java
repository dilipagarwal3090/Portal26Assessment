package com.portal26.translator.exception;

import lombok.Getter;
import lombok.NonNull;

/**
 * Custom Exception for all conflict exceptions.
 */
@Getter
public class InternalConflictException extends RuntimeException {

    /**
     * Error code.
     */
    private final String errorCode;

    /**
     * Internal conflict exception with message, code and cause.
     *
     * @param message   exception message
     * @param cause     exception cause
     * @param errorCode exception errorCode
     */
    public InternalConflictException(@NonNull final String message, @NonNull final Exception cause,
            @NonNull final String errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    /**
     * Internal conflict exception with cause and errorCode.
     *
     * @param cause     exception cause
     * @param errorCode exception errorCode
     */
    public InternalConflictException(@NonNull final Throwable cause, @NonNull final String errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    /**
     * Internal conflict exception with message and errorCode.
     *
     * @param message   exception message
     * @param errorCode exception errorCode
     */
    public InternalConflictException(@NonNull final String message, @NonNull final String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Internal conflict exception with message and cause.
     *
     * @param message exception message
     * @param cause   exception cause
     */
    public InternalConflictException(@NonNull final String message, @NonNull final Exception cause) {
        super(message, cause);
        this.errorCode = cause.getMessage();
    }
}
