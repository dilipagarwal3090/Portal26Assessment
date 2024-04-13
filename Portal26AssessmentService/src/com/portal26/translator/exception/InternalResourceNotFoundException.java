package com.portal26.translator.exception;

import lombok.Getter;
import lombok.NonNull;

/**
 * Exception for Null Response.
 */
@Getter
public class InternalResourceNotFoundException extends RuntimeException {
    /**
     * Error code.
     */
    final String errorCode;

    /**
     * InternalResourceNotFoundException.
     *
     * @param message   exception message
     * @param cause     exception cause
     * @param errorCode exception errorCode
     */
    public InternalResourceNotFoundException(@NonNull final String message, @NonNull final Exception cause,
            @NonNull final String errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    /**
     * InternalResourceNotFoundException.
     *
     * @param cause     exception cause
     * @param errorCode exception errorCode
     */
    public InternalResourceNotFoundException(@NonNull final Throwable cause, @NonNull final String errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    /**
     * InternalResourceNotFoundException.
     *
     * @param message   exception message
     * @param errorCode exception errorCode
     */
    public InternalResourceNotFoundException(@NonNull final String message, @NonNull final String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
