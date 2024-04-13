package com.portal26.handler;

import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.portal26.component.QueryEventComponent;
import com.portal26.constants.ErrorCodes;
import com.portal26.dagger.DaggerQueryEventHandlerComponent;
import com.portal26.dagger.QueryEventHandlerComponent;
import com.portal26.handler.validation.QueryEventValidationRequestValidator;
import com.portal26.model.EventData;
import com.portal26.model.QueryEventRequest;
import com.portal26.translator.ExceptionTranslator;
import com.portal26.translator.exception.InternalBadRequestException;
import com.portal26.translator.exception.InternalNonRetryableException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

import static com.portal26.constants.ServiceConstants.APPLICATION_JSON_HEADERS;

/**
 * QueryEventHandler to query events in application
 */
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class QueryEventHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    QueryEventComponent queryEventComponent;
    ObjectMapper objectMapper;
    ExceptionTranslator exceptionTranslator;
    QueryEventValidationRequestValidator queryEventValidationRequestValidator;

    public QueryEventHandler() {
        final QueryEventHandlerComponent component = DaggerQueryEventHandlerComponent.create();
        this.queryEventComponent = component.queryEventComponent();
        this.objectMapper = component.objectMapper();
        this.exceptionTranslator = component.exceptionTranslator();
        this.queryEventValidationRequestValidator = component.queryEventValidationRequestValidator();
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, Context context) {
        try {
            log.info("APIGatewayProxyResponseEvent input: {}", input);
            final QueryEventRequest queryEventRequest = convertBodyToQueryEventRequest(input.getBody());
            log.info("Query event request: {}", queryEventRequest);
            queryEventValidationRequestValidator.validateLambdaRequest(queryEventRequest);
            log.info("Basic validation passed for queryEventRequest");
            final List<EventData> eventData = queryEventComponent.getEvents(queryEventRequest);
            return createAPIGatewayProxyResponseEvent(convertEventDataListToJson(eventData), 200);
        } catch (final InternalBadRequestException exception) {
            log.error("InternalBadRequestException occurred for input: {} with trace {}", input,
                    ExceptionUtils.getStackTrace(exception));
            return createAPIGatewayProxyResponseEvent(exception.getMessage(), 400);
        } catch (final InternalNonRetryableException exception) {
            log.error("InternalNonRetryableException occurred for input: {} with trace {}", input,
                    ExceptionUtils.getStackTrace(exception));
            return createAPIGatewayProxyResponseEvent(exception.getMessage(), 500);
        } catch (final RuntimeException exception) {
            log.error("RuntimeException occurred for input: {} with trace {}", input,
                    ExceptionUtils.getStackTrace(exception));
            return createAPIGatewayProxyResponseEvent(exception.getMessage(), 500);
        }
    }

    private QueryEventRequest convertBodyToQueryEventRequest(final String requestBody) {
        try {
            return objectMapper.readValue(requestBody, QueryEventRequest.class);
        } catch (final JsonProcessingException exception) {
            log.error("Unexpected error when trying to deserialize request: {}", requestBody);
            throw new InternalNonRetryableException(exception, ErrorCodes.REQUEST_DESERIALIZATION_ERROR.getValue());
        } catch (final RuntimeException exception) {
            log.error("Exception Occurred for input: {} with trace {}", requestBody,
                    ExceptionUtils.getStackTrace(exception));
            throw this.exceptionTranslator.translate(exception, exception.getMessage());
        }
    }

    private APIGatewayProxyResponseEvent createAPIGatewayProxyResponseEvent(final String responseBody,
            final int statusCode) {
        final APIGatewayProxyResponseEvent proxyResponseEvent = new APIGatewayProxyResponseEvent();
        proxyResponseEvent.setStatusCode(statusCode);
        proxyResponseEvent.setBody(responseBody);
        proxyResponseEvent.setHeaders(APPLICATION_JSON_HEADERS);
        return proxyResponseEvent;
    }

    private static String convertEventDataListToJson(final List<EventData> eventDataList) {
        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(eventDataList);
        } catch (JsonProcessingException exception) {
            log.error("Unexpected error when trying to deserialize eventDataList: {}", eventDataList);
            throw new InternalNonRetryableException(exception, ErrorCodes.REQUEST_DESERIALIZATION_ERROR.getValue());
        }
    }
}