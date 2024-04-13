package com.portal26.translator.exception;

import lombok.Getter;
import lombok.NonNull;

/**
 * Custom Exception for all Non-Retryable exceptions.
 */
@Getter
public class InternalNonRetryableException extends RuntimeException {

    /**
     * Error code.
     */
    private final String errorCode;

    /**
     * Non retryable exception with message, cause and errorCode.
     *
     * @param message   exception message
     * @param cause     exception cause
     * @param errorCode exception code
     */
    public InternalNonRetryableException(@NonNull final String message, @NonNull final Exception cause,
            @NonNull final String errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    /**
     * Non retryable exception with cause and errorCode.
     *
     * @param cause     exception cause
     * @param errorCode exception code
     */

    public InternalNonRetryableException(@NonNull final Throwable cause, @NonNull final String errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    /**
     * Non retryable exception with message and errorCode.
     *
     * @param message   exception message
     * @param errorCode exception code
     */
    public InternalNonRetryableException(@NonNull final String message, @NonNull final String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Non retryable exception with message and cause.
     *
     * @param message exception message
     * @param cause   exception cause
     */
    public InternalNonRetryableException(@NonNull final String message, @NonNull final Exception cause) {
        super(message, cause);
        this.errorCode = cause.getMessage();
    }
}