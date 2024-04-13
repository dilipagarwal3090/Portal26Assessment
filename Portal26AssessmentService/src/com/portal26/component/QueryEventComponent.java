package com.portal26.component;

import com.portal26.constants.ErrorCodes;
import com.portal26.dao.EventDetailsDao;
import com.portal26.dao.ddbmodel.EventDetails;
import com.portal26.dao.model.QueryEventDaoRequest;
import com.portal26.model.CreateEventRequest;
import com.portal26.model.EventData;
import com.portal26.model.QueryEventRequest;
import com.portal26.translator.exception.InternalBadRequestException;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.portal26.constants.ServiceConstants.FROM_DATE_FORMAT;
import static com.portal26.constants.ServiceConstants.TO_DATE_FORMAT;

@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class QueryEventComponent {

    EventDetailsDao eventDetailsDao;

    public List<EventData> getEvents(@NonNull final QueryEventRequest request) {
        final QueryEventDaoRequest queryEventDaoRequest = getQueryEventDaoRequest(request);
        log.info("Query event dao request: {}", queryEventDaoRequest);
        final List<EventDetails> eventDetails = eventDetailsDao.getEvents(queryEventDaoRequest);
        return convertToEventData(eventDetails);
    }

    private QueryEventDaoRequest getQueryEventDaoRequest(final QueryEventRequest request) {
        final Long startTime = convertStringToEpoch(request.getFromDate(), FROM_DATE_FORMAT);
        final Long endTime = convertStringToEpoch(request.getToDate(), TO_DATE_FORMAT);
        return QueryEventDaoRequest.builder().startTime(startTime).endTime(endTime).userId(request.getUserId())
                .domain(request.getDomain()).category(request.getCategory()).build();
    }

    private static long convertStringToEpoch(final String dateString, final String format) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        final LocalDate date = LocalDate.parse(dateString, formatter);
        return date.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();
    }

    public static List<EventData> convertToEventData(List<EventDetails> eventDetailsList) {
        List<EventData> eventDataList = new ArrayList<>();
        for (EventDetails eventDetails : eventDetailsList) {
            eventDataList.add(convertEventDetailsToEventData(eventDetails));
        }
        return eventDataList;
    }

    private static EventData convertEventDetailsToEventData(EventDetails eventDetails) {
        return new EventData(eventDetails.getActualEventTimeStamp(), eventDetails.getUserId(), eventDetails.getBody());
    }
}
