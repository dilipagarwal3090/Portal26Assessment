package com.portal26;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.local.embedded.DynamoDBEmbedded;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteTableRequest;
import com.amazonaws.services.dynamodbv2.model.GlobalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.LocalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.Projection;
import com.amazonaws.services.dynamodbv2.model.ProjectionType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.portal26.dao.ddbmodel.EventDetails;

import java.util.List;

/**
 * Class to provide local ddb
 */
public class LocalDdbSetup {
    private static final Class[] DYNAMODB_TABLE_DEFINITIONS = { EventDetails.class };
    private static final int MAX_RETRY_DDB = 3;
    private static final long READ_CAPACITY_UNITS = 100L;
    private static final long WRITE_CAPACITY_UNITS = 100L;
    private final AmazonDynamoDB dynamoDB = DynamoDBEmbedded.create().amazonDynamoDB();
    private static final DynamoDBMapperConfig defaultMapperConfig = DynamoDBMapperConfig.builder()
            .withPaginationLoadingStrategy(DynamoDBMapperConfig.PaginationLoadingStrategy.LAZY_LOADING)
            .withBatchWriteRetryStrategy(new DynamoDBMapperConfig.DefaultBatchWriteRetryStrategy(MAX_RETRY_DDB))
            .withBatchLoadRetryStrategy(new DynamoDBMapperConfig.DefaultBatchLoadRetryStrategy())
            .withConsistentReads(DynamoDBMapperConfig.ConsistentReads.CONSISTENT)
            .withSaveBehavior(DynamoDBMapperConfig.SaveBehavior.UPDATE_SKIP_NULL_ATTRIBUTES).build();
    private final DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB, defaultMapperConfig);

    public void createTables() {
        for (final Class clazz : DYNAMODB_TABLE_DEFINITIONS) {
            final CreateTableRequest createTableRequest = mapper.generateCreateTableRequest(clazz);
            createTableRequest.setProvisionedThroughput(
                    new ProvisionedThroughput(READ_CAPACITY_UNITS, WRITE_CAPACITY_UNITS));
            final List<GlobalSecondaryIndex> gsiList = createTableRequest.getGlobalSecondaryIndexes();
            final List<LocalSecondaryIndex> lsiList = createTableRequest.getLocalSecondaryIndexes();
            if (gsiList != null) {
                gsiList.forEach(index -> {
                    index.setProjection(new Projection().withProjectionType(ProjectionType.ALL));
                    index.setProvisionedThroughput(
                            new ProvisionedThroughput(READ_CAPACITY_UNITS, WRITE_CAPACITY_UNITS));
                });
            }
            if (lsiList != null) {
                lsiList.forEach(index -> {
                    index.setProjection(new Projection().withProjectionType(ProjectionType.ALL));
                });
            }
            dynamoDB.createTable(createTableRequest);
        }
    }

    public DynamoDBMapper getLocalDynamoDBMapper() {
        return mapper;
    }

    public AmazonDynamoDB getLocalAmazonDynamoDB() {
        return dynamoDB;
    }
}
