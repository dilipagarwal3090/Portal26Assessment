package com.portal26.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.portal26.constants.ErrorCodes;
import com.portal26.dao.ddbmodel.EventDetails;
import com.portal26.dao.model.QueryEventDaoRequest;
import com.portal26.translator.DynamoDBExceptionTranslator;
import com.portal26.translator.exception.InternalBadRequestException;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Event details dao to access the EventDetails database table.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class EventDetailsDao {

    DynamoDBMapper dynamoDBMapper;
    DynamoDBExceptionTranslator dynamoDBExceptionTranslator;

    /**
     * Method used to save events details in database
     *
     * @param eventDetails, EventDetails
     * @return eventId, String
     */
    public String createEvent(@NonNull final EventDetails eventDetails) {
        try {
            log.info("Saving eventDetails in database: {}", eventDetails);
            dynamoDBMapper.save(eventDetails);
            return eventDetails.getEventId();
        } catch (final Exception exception) {
            log.error("Error occurred while saving eventDetails for input : {}, stacktrace : {}", eventDetails,
                    ExceptionUtils.getStackTrace(exception));
            throw this.dynamoDBExceptionTranslator.translate(exception, exception.getMessage());
        }
    }

    /**
     * Method used to get all events based on request
     *
     * @param request, QueryEventDaoRequest
     * @return List<EventDetails>, list of EventDetails
     */
    public List<EventDetails> getEvents(final QueryEventDaoRequest request) {
        if (request.getStartTime() > request.getEndTime()) {
            throw new InternalBadRequestException("Invalid timeRange", ErrorCodes.INVALID_TIME_RANGE.getValue());
        } else {
            return getEventsByUserTimeRange(request);
        }
    }

    private List<EventDetails> getEventsByUserTimeRange(final QueryEventDaoRequest request) {
        try {
            String filterExpression = "";
            Map<String, String> expressionAttributeNames = new HashMap<>();
            Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();

            expressionAttributeValues.put(":start", new AttributeValue().withN(String.valueOf(request.getStartTime())));
            expressionAttributeValues.put(":end", new AttributeValue().withN(String.valueOf(request.getEndTime())));

            filterExpression += "eventTimeStamp between :start and :end and ";

            if (request.getUserId() != null) {
                expressionAttributeValues.put(":userId", new AttributeValue().withS(request.getUserId()));
                filterExpression += "#userId = :userId and ";
                expressionAttributeNames.put("#userId", "userId");
            }
            if (request.getDomain() != null) {
                expressionAttributeValues.put(":domain", new AttributeValue().withS(request.getDomain()));
                filterExpression += "#domain = :domain and ";
                expressionAttributeNames.put("#domain", "domain");
            }
            filterExpression = filterExpression.substring(0, filterExpression.length() - 5);
            log.info("filterExpression: {}", filterExpression);
            log.info("expressionAttributeNames: {}", expressionAttributeNames);
            log.info("expressionAttributeValues: {}", expressionAttributeValues);
            DynamoDBScanExpression scanExpression = new DynamoDBScanExpression().withFilterExpression(filterExpression)
                    .withExpressionAttributeValues(expressionAttributeValues);
            if (!expressionAttributeNames.isEmpty()) {
                scanExpression = scanExpression.withExpressionAttributeNames(expressionAttributeNames);
            }
            return dynamoDBMapper.scan(EventDetails.class, scanExpression);
        } catch (final Exception exception) {
            log.error("Error occurred while fetching eventDetails for request : {}, stacktrace : {}", request,
                    ExceptionUtils.getStackTrace(exception));
            throw this.dynamoDBExceptionTranslator.translate(exception, exception.getMessage());
        }
    }
}
