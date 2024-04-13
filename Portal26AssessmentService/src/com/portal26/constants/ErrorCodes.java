package com.portal26.constants;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

/**
 * Error Codes.
 */
@AllArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ErrorCodes {
    REQUEST_DESERIALIZATION_ERROR("101"),
    INVALID_URL("102"),
    INVALID_EVENT_TIMESTAMP("103"),
    INVALID_FROM_DATE_FORMAT("104"),
    INVALID_TO_DATE_FORMAT("105"),
    INVALID_TIME_RANGE("106");
    final String value;
}