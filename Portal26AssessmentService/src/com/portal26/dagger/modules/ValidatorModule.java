package com.portal26.dagger.modules;

import com.portal26.handler.validation.QueryEventValidationRequestValidator;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

/**
 * Validation Module
 */
@Module
public class ValidatorModule {

    @Provides
    @Singleton
    public QueryEventValidationRequestValidator getQueryEventValidationRequestValidator() {
        return new QueryEventValidationRequestValidator();
    }
}
