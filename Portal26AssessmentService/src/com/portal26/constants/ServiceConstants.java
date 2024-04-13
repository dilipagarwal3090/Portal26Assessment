package com.portal26.constants;

import lombok.experimental.UtilityClass;

import java.util.Map;

/**
 * Service constants used by service.
 */
@UtilityClass
public class ServiceConstants {
    public static final String FROM_DATE_FORMAT = "yyyy-MM-dd";
    public static final String TO_DATE_FORMAT = "yyyy-MM-dd";
    public static final Map<String, String> APPLICATION_JSON_HEADERS = Map.of("Content-Type", "application/json");
    public static final int TIME_TO_LIVE_IN_YEARS = 3;
}
