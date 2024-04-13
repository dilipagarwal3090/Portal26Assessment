package com.portal26.dagger;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.portal26.component.QueryEventComponent;
import com.portal26.dagger.modules.AwsDynamoDBModule;
import com.portal26.dagger.modules.MapperModule;
import com.portal26.handler.validation.QueryEventValidationRequestValidator;
import com.portal26.translator.ExceptionTranslator;
import dagger.Component;

import javax.inject.Singleton;

/**
 * Dagger component for QueryEventHandler
 */
@Singleton
@Component(modules = { AwsDynamoDBModule.class, MapperModule.class })
public interface QueryEventHandlerComponent {

    QueryEventComponent queryEventComponent();

    DynamoDBMapper dynamoDBMapper();

    ObjectMapper objectMapper();

    ExceptionTranslator exceptionTranslator();

    QueryEventValidationRequestValidator queryEventValidationRequestValidator();
}