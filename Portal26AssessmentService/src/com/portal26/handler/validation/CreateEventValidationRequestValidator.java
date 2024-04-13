package com.portal26.handler.validation;

import com.portal26.model.CreateEventRequest;
import lombok.NonNull;

import javax.inject.Inject;

/**
 * CreateEvent Validation Request Validator
 */
public class CreateEventValidationRequestValidator extends ValidationRequestBaseValidator {

    @Inject
    public CreateEventValidationRequestValidator() {
    }

    /**
     * Method to validate CreateEventRequest
     *
     * @param request, CreateEventRequest
     */
    public void validateLambdaRequest(@NonNull final CreateEventRequest request) {
        super.validateEventTimestamp(request.getEventTimestamp());
        super.validateURL(request.getUrl());
        super.validateBody(request.getBody());
        super.validateUserId(request.getUserId());
    }
}
