package com.portal26.translator;

import com.portal26.translator.exception.ConflictException;
import com.portal26.translator.exception.InternalBadRequestException;
import com.portal26.translator.exception.InternalConflictException;
import com.portal26.translator.exception.InternalNonRetryableException;
import com.portal26.translator.exception.InternalResourceNotFoundException;
import com.portal26.translator.exception.InternalRetryableException;
import com.portal26.translator.exception.InternalServerException;
import com.portal26.translator.exception.InvalidRequestException;
import com.portal26.translator.exception.NonRecoverableException;
import com.portal26.translator.exception.RecoverableException;
import com.portal26.translator.exception.ResourceNotFoundException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * Class to provide default exception translation from exceptionTranslator.
 * Exception translations specific to service should be defined in service
 */
@Slf4j
public class ExceptionTranslator {
    private static final String INTERNAL_SERVER_ERROR = "Internal server error";

    /**
     * Translates Exceptions thrown by dependency layers of Activity.
     *
     * @param originalException : Original Exception.
     * @param message           : message
     * @return : translated Exception.
     */
    public RuntimeException translate(@NonNull final RuntimeException originalException,
            @NonNull final String message) {
        log.info("ActivityExceptionTranslator: Exception before translation: {}", message);

        // Handling already translated exceptions
        if (checkIfAlreadyTranslatedServerSideException(originalException)
                || checkIfAlreadyTranslatedClientSideException(originalException))
            return originalException;

        if (originalException instanceof InternalBadRequestException
                || originalException instanceof InternalResourceNotFoundException
                || originalException instanceof InternalConflictException) {
            return translateClientSideException(originalException, message);
        }

        return (originalException instanceof InternalRetryableException
                || originalException instanceof InternalNonRetryableException) ?
                translateServerSideException(originalException, message) :
                new InternalServerException(INTERNAL_SERVER_ERROR, originalException);
    }

    private static boolean checkIfAlreadyTranslatedClientSideException(RuntimeException originalException) {

        return originalException instanceof InvalidRequestException || originalException instanceof ConflictException
                || originalException instanceof ResourceNotFoundException;
    }

    private static boolean checkIfAlreadyTranslatedServerSideException(RuntimeException originalException) {

        return originalException instanceof NonRecoverableException || originalException instanceof RecoverableException
                || originalException instanceof InternalServerException;
    }

    private RuntimeException translateServerSideException(RuntimeException originalException, String message) {
        return (originalException instanceof InternalRetryableException) ?
                this.buildRecoverableException((InternalRetryableException) originalException, message) :
                this.buildNonRecoverableException((InternalNonRetryableException) originalException, message);
    }

    private RuntimeException translateClientSideException(RuntimeException originalException, String message) {
        if (originalException instanceof InternalConflictException)
            return this.buildConflictException((InternalConflictException) originalException, message);

        return (originalException instanceof InternalBadRequestException) ?
                this.buildBadRequestException((InternalBadRequestException) originalException, message) :
                this.buildResourceNotFoundException((InternalResourceNotFoundException) originalException, message);
    }

    private ConflictException buildConflictException(final InternalConflictException originalException,
            String message) {

        final ConflictException translatedException = new ConflictException(message, originalException);
        translatedException.setCode(originalException.getErrorCode());
        return translatedException;
    }

    private InvalidRequestException buildBadRequestException(final InternalBadRequestException originalException,
            final String message) {

        final InvalidRequestException translatedException = new InvalidRequestException(message, originalException);
        translatedException.setCode(originalException.getErrorCode());
        return translatedException;
    }

    private NonRecoverableException buildNonRecoverableException(final InternalNonRetryableException originalException,
            final String message) {
        final NonRecoverableException translatedException = new NonRecoverableException(message, originalException);
        translatedException.setCode(originalException.getErrorCode());

        return translatedException;
    }

    private RecoverableException buildRecoverableException(final InternalRetryableException originalException,
            final String message) {
        final RecoverableException translatedException = new RecoverableException(message, originalException);
        translatedException.setCode(originalException.getErrorCode());

        return translatedException;
    }

    private ResourceNotFoundException buildResourceNotFoundException(
            final InternalResourceNotFoundException originalException, final String message) {
        final ResourceNotFoundException translatedException = new ResourceNotFoundException(message, originalException);
        translatedException.setCode(originalException.getErrorCode());
        return translatedException;
    }
}
