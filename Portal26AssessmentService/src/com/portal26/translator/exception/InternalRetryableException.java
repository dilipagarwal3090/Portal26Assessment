package com.portal26.translator.exception;

import lombok.Getter;
import lombok.NonNull;

/**
 * Custom exception for all retryable exception.
 * translated at activity level.
 */
@Getter
public class InternalRetryableException extends RuntimeException {

    /**
     * Error code.
     */
    private final String errorCode;

    /**
     * Retryable exception.
     *
     * @param message   exception message
     * @param cause     exception cause
     * @param errorCode exception errorCode
     */
    public InternalRetryableException(@NonNull final String message, @NonNull final Exception cause,
            @NonNull final String errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    /**
     * Retryable exception with cause and errorCode.
     *
     * @param cause     exception cause
     * @param errorCode exception errorCode
     */
    public InternalRetryableException(@NonNull final Throwable cause, @NonNull final String errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    /**
     * Retryable exception with message and errorCode.
     *
     * @param message   exception message
     * @param errorCode exception errorCode
     */
    public InternalRetryableException(@NonNull final String message, @NonNull final String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Retryable exception with message and cause.
     *
     * @param message exception message
     * @param cause   exception cause
     */
    public InternalRetryableException(@NonNull final String message, @NonNull final Exception cause) {
        super(message, cause);
        this.errorCode = cause.getMessage();
    }
}
