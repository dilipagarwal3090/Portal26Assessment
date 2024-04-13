package com.portal26.dagger.modules;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.portal26.translator.ExceptionTranslator;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

/**
 * Mapper Module
 */
@Module
public class MapperModule {

    @Provides
    @Singleton
    public ObjectMapper getObjectMapper() {
        return new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Provides
    @Singleton
    public ExceptionTranslator getActivityExceptionTranslator() {
        return new ExceptionTranslator();
    }
}
