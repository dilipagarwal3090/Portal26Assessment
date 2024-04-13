package com.portal26.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.portal26.component.CreateEventComponent;
import com.portal26.constants.ErrorCodes;
import com.portal26.dagger.CreateEventHandlerComponent;
import com.portal26.dagger.DaggerCreateEventHandlerComponent;
import com.portal26.handler.validation.CreateEventValidationRequestValidator;
import com.portal26.model.CreateEventRequest;
import com.portal26.translator.ExceptionTranslator;
import com.portal26.translator.exception.InternalBadRequestException;
import com.portal26.translator.exception.InternalNonRetryableException;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * CreateEventHandler to create events in application
 */
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CreateEventHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    CreateEventComponent createEventComponent;
    ObjectMapper objectMapper;
    ExceptionTranslator exceptionTranslator;
    CreateEventValidationRequestValidator createEventValidationRequestValidator;

    public CreateEventHandler() {
        final CreateEventHandlerComponent component = DaggerCreateEventHandlerComponent.create();
        this.createEventComponent = component.createEventComponent();
        this.objectMapper = component.objectMapper();
        this.exceptionTranslator = component.exceptionTranslator();
        this.createEventValidationRequestValidator = component.createEventValidationRequestValidator();
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(@NonNull final APIGatewayProxyRequestEvent input,
            @NonNull final Context context) {
        try {
            log.info("APIGatewayProxyResponseEvent input: {}", input);
            final CreateEventRequest createEventRequest = convertBodyToCreateEventRequest(input.getBody());
            log.info("Create event request: {}", createEventRequest);
            createEventValidationRequestValidator.validateLambdaRequest(createEventRequest);
            log.info("Basic validation passed for createEventRequest");
            createEventComponent.createEvent(createEventRequest);
            return createAPIGatewayProxyResponseEvent("success", 200);
        } catch (final InternalBadRequestException exception) {
            log.error("InternalBadRequestException Occurred for input: {} with trace {}", input,
                    ExceptionUtils.getStackTrace(exception));
            return createAPIGatewayProxyResponseEvent(exception.getMessage(), 400);
        } catch (final InternalNonRetryableException exception) {
            log.error("InternalBadRequestException Occurred for input: {} with trace {}", input,
                    ExceptionUtils.getStackTrace(exception));
            return createAPIGatewayProxyResponseEvent(exception.getMessage(), 500);
        } catch (final RuntimeException exception) {
            log.error("Exception Occurred for input: {} with trace {}", exception,
                    ExceptionUtils.getStackTrace(exception));
            return createAPIGatewayProxyResponseEvent(exception.getMessage(), 500);
        }
    }

    private CreateEventRequest convertBodyToCreateEventRequest(final String requestBody) {
        try {
            log.info("RequestBody: {}", requestBody);
            return objectMapper.readValue(requestBody, CreateEventRequest.class);
        } catch (final JsonProcessingException exception) {
            log.error("JsonProcessingException occurred when trying to deserialize requestBody: {}", requestBody);
            throw new InternalNonRetryableException(exception, ErrorCodes.REQUEST_DESERIALIZATION_ERROR.getValue());
        } catch (final RuntimeException exception) {
            log.error("RuntimeException occurred for requestBody: {} with trace {}", requestBody,
                    ExceptionUtils.getStackTrace(exception));
            throw this.exceptionTranslator.translate(exception, exception.getMessage());
        }
    }

    private APIGatewayProxyResponseEvent createAPIGatewayProxyResponseEvent(final String responseBody,
            final int responseStatusCode) {
        log.info("Create APIGatewayProxyResponseEvent for responseBody: {} and responseStatusCode: {}", responseBody,
                responseStatusCode);
        final APIGatewayProxyResponseEvent proxyResponseEvent = new APIGatewayProxyResponseEvent();
        proxyResponseEvent.setStatusCode(responseStatusCode);
        proxyResponseEvent.setBody(responseBody);
        return proxyResponseEvent;
    }
}