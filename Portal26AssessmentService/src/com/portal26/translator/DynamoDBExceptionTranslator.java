package com.portal26.translator;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughputExceededException;
import com.portal26.translator.exception.InternalNonRetryableException;
import com.portal26.translator.exception.InternalRetryableException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Exception translator for DynamoDB exceptions
 * <a href="https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/Programming.Errors.html">...</a>
 */
public class DynamoDBExceptionTranslator {

    private static List<Class<? extends Exception>> retryableExceptions = new ArrayList<>();

    static {
        retryableExceptions.add(SocketException.class);
        retryableExceptions.add(SocketTimeoutException.class);
        retryableExceptions.add(IOException.class);
        retryableExceptions.add(ProvisionedThroughputExceededException.class);
        retryableExceptions = Collections.unmodifiableList(retryableExceptions);
    }

    static final Set<String> RETRYABLE_ERROR_CODES = new HashSet();

    static {
        RETRYABLE_ERROR_CODES.add("Throttling");
        RETRYABLE_ERROR_CODES.add("ThrottlingException");
        RETRYABLE_ERROR_CODES.add("ThrottledException");
        RETRYABLE_ERROR_CODES.add("ProvisionedThroughputExceededException");
        RETRYABLE_ERROR_CODES.add("SlowDown");
        RETRYABLE_ERROR_CODES.add("TooManyRequestsException");
        RETRYABLE_ERROR_CODES.add("RequestLimitExceeded");
        RETRYABLE_ERROR_CODES.add("BandwidthLimitExceeded");
        RETRYABLE_ERROR_CODES.add("RequestThrottled");
    }

    /**
     * @param originalException : Exception to be translated.
     * @param message           exception message
     * @return RuntimeException object
     */
    public RuntimeException translate(@NonNull final Exception originalException, @NonNull final String message) {
        final String newMessage = String.format("%s, Exception message %s", message, originalException.getMessage());
        if (retryableExceptions.stream().anyMatch(x -> x.isAssignableFrom(originalException.getClass()))) {
            return new InternalRetryableException(newMessage, originalException);
        }

        if (originalException instanceof AmazonServiceException) {
            if (((AmazonServiceException) originalException).getErrorType()
                    == AmazonServiceException.ErrorType.Client) {
                return new InternalNonRetryableException(
                        String.format("Client exception from DynamoDB, error found in the caller request : %s",
                                newMessage), originalException);
            }
            return new InternalRetryableException(
                    String.format("Service/Unknown exception from DynamoDB : %s", newMessage), originalException);
        }

        return originalException instanceof InternalNonRetryableException ?
                (InternalNonRetryableException) originalException :
                new InternalNonRetryableException(String.format("Exception in data access layer : %s", newMessage),
                        originalException);
    }
}