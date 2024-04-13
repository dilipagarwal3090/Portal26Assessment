package com.portal26.component;

import com.portal26.constants.ErrorCodes;
import com.portal26.dao.EventDetailsDao;
import com.portal26.dao.ddbmodel.EventDetails;
import com.portal26.model.CreateEventRequest;
import com.portal26.translator.exception.InternalBadRequestException;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.portal26.constants.ServiceConstants.TIME_TO_LIVE_IN_YEARS;

/**
 * Create event component
 */
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class CreateEventComponent {

    EventDetailsDao eventDetailsDao;

    public void createEvent(@NonNull final CreateEventRequest request) {
        final EventDetails eventDetails = convertRequestToEventDetails(request);
        log.info("Converted eventDetails from CreateEventRequest {}", eventDetails);
        eventDetailsDao.createEvent(eventDetails);
    }

    private EventDetails convertRequestToEventDetails(final CreateEventRequest request) {
        final String eventId = generateUniqueId();
        final OffsetDateTime offsetDateTime = OffsetDateTime.parse(request.getEventTimestamp(),
                DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        final String domain = extractDomainName(request.getUrl());
        final Long eventTimeStamp = offsetDateTime.toInstant().toEpochMilli();
        final String category = getCategory(request);
        return EventDetails.builder().eventId(eventId).userId(request.getUserId()).domain(domain).url(request.getUrl())
                .body(request.getBody()).eventTimeStamp(eventTimeStamp)
                .actualEventTimeStamp(request.getEventTimestamp()).category(category).ttl(getTTL(TIME_TO_LIVE_IN_YEARS))
                .build();
    }

    private String extractDomainName(final String url) {
        final Pattern pattern = Pattern.compile(
                "^(?:https?:\\/\\/)?(?:www:\\/\\/)?(?:www\\.)?(?:[^\\/]+\\.)?([^\\.\\/]+\\.[^\\/]+)");
        final Matcher matcher = pattern.matcher(url);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            throw new InternalBadRequestException("Please provide valid url", ErrorCodes.INVALID_URL.getValue());
        }
    }

    private String getCategory(final CreateEventRequest request) {
        return "Electronics";
    }

    private String generateUniqueId() {
        return System.currentTimeMillis() + "-" + UUID.randomUUID();
    }

    private Long getTTL(final int years) {
        final Instant now = Instant.now();
        final LocalDateTime currentDateTime = LocalDateTime.ofInstant(now, ZoneOffset.UTC);
        final LocalDateTime futureDateTime = currentDateTime.plus(years, ChronoUnit.YEARS);
        return futureDateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
    }
}
