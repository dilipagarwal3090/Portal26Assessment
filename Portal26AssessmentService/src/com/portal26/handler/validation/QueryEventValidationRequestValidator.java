package com.portal26.handler.validation;

import com.portal26.model.QueryEventRequest;
import lombok.NonNull;

import javax.inject.Inject;

/**
 * QueryEvent Validation Request Validator
 */
public class QueryEventValidationRequestValidator extends ValidationRequestBaseValidator {

    @Inject
    public QueryEventValidationRequestValidator() {
    }

    /**
     * Method to validate CreateEventRequest
     *
     * @param request, CreateEventRequest
     */
    public void validateLambdaRequest(@NonNull final QueryEventRequest request) {
        super.validateFromDate(request.getFromDate());
        super.validateToDate(request.getToDate());
        super.validateDomain(request.getDomain());
        super.validateUserId(request.getUserId());
        super.validateCategory(request.getCategory());
    }
}
