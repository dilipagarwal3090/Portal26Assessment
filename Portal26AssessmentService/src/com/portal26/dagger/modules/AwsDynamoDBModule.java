package com.portal26.dagger.modules;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.retry.PredefinedRetryPolicies;
import com.amazonaws.retry.RetryPolicy;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.portal26.translator.DynamoDBExceptionTranslator;
import dagger.Module;
import dagger.Provides;
import lombok.NonNull;

import javax.inject.Singleton;

/**
 * AWS AwsDynamoDB Module
 */
@Module
public class AwsDynamoDBModule {

    private static final int MAX_RETRY_DDB = 3;
    private static final boolean HONOR_MAX_RETRY = true;
    private static final int MAX_CONNECTION = 200;
    private static final String AWS_REGION = "eu-west-1";

    private static final DynamoDBMapperConfig defaultMapperConfig = DynamoDBMapperConfig.builder()
            .withPaginationLoadingStrategy(DynamoDBMapperConfig.PaginationLoadingStrategy.LAZY_LOADING)
            .withBatchWriteRetryStrategy(new DynamoDBMapperConfig.DefaultBatchWriteRetryStrategy(MAX_RETRY_DDB))
            .withBatchLoadRetryStrategy(new DynamoDBMapperConfig.DefaultBatchLoadRetryStrategy())
            .withConsistentReads(DynamoDBMapperConfig.ConsistentReads.CONSISTENT)
            .withSaveBehavior(DynamoDBMapperConfig.SaveBehavior.UPDATE_SKIP_NULL_ATTRIBUTES).build();

    @Provides
    @Singleton
    public ClientConfiguration provideDynamoDBClientConfig() {
        return new ClientConfiguration().withRetryPolicy(
                        new RetryPolicy(PredefinedRetryPolicies.DEFAULT_RETRY_CONDITION,
                                PredefinedRetryPolicies.DEFAULT_BACKOFF_STRATEGY, MAX_RETRY_DDB, HONOR_MAX_RETRY))
                .withMaxConnections(MAX_CONNECTION);
    }

    @Provides
    @Singleton
    public AmazonDynamoDB provideAmazonDynamoDB() {
        return AmazonDynamoDBClientBuilder.standard().withRegion(AWS_REGION)
                .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
                .withClientConfiguration(provideDynamoDBClientConfig()).build();
    }

    @Provides
    @Singleton
    public DynamoDBMapper provideDynamoDBMapper(@NonNull final AmazonDynamoDB amazonDynamoDB) {
        return new DynamoDBMapper(amazonDynamoDB, defaultMapperConfig);
    }

    @Provides
    @Singleton
    public DynamoDBExceptionTranslator provideDynamoDBExceptionTranslator() {
        return new DynamoDBExceptionTranslator();
    }
}
