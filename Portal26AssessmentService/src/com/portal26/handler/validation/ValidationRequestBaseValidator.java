package com.portal26.handler.validation;

import com.portal26.constants.ErrorCodes;
import com.portal26.translator.exception.InternalBadRequestException;
import lombok.NonNull;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Base Validator class
 */
public class ValidationRequestBaseValidator {

    /**
     * Validation function for event timestamp
     *
     * @param eventTimeStamp
     */
    public void validateEventTimestamp(@NonNull final String eventTimeStamp) {
        String regex = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{6}\\+\\d{2}:\\d{2}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(eventTimeStamp);
        if (!matcher.matches()) {
            throw new InternalBadRequestException("Invalid event timestamp",
                    ErrorCodes.INVALID_EVENT_TIMESTAMP.getValue());
        }
    }

    /**
     * Validation function for fromDate field
     *
     * @param fromDate
     */
    public void validateFromDate(@NonNull final String fromDate) {
        try {
            LocalDate.parse(fromDate);
        } catch (DateTimeParseException e) {
            throw new InternalBadRequestException("Invalid fromDate format, Expected format: yyyy-MM-dd",
                    ErrorCodes.INVALID_FROM_DATE_FORMAT.getValue());
        }
    }

    /**
     * Validation function for toDate field
     *
     * @param toDate
     */
    public void validateToDate(@NonNull final String toDate) {
        try {
            LocalDate.parse(toDate);
        } catch (DateTimeParseException e) {
            throw new InternalBadRequestException("Invalid toDate format, Expected format: yyyy-MM-dd",
                    ErrorCodes.INVALID_TO_DATE_FORMAT.getValue());
        }
    }

    /**
     * Validation function for domain field
     *
     * @param domain
     */
    public void validateDomain(final String domain) {

    }

    /**
     * Validation function for userId field
     *
     * @param userId
     */
    public void validateUserId(final String userId) {

    }

    /**
     * Validation function for category field
     *
     * @param category
     */
    public void validateCategory(final String category) {

    }

    /**
     * Validation function for url field
     *
     * @param url
     */
    public void validateURL(@NonNull final String url) {

    }

    /**
     * Validation function for body field
     *
     * @param body
     */
    public void validateBody(@NonNull final String body) {

    }
}
