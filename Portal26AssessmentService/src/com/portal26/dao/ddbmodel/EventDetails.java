package com.portal26.dao.ddbmodel;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBVersionAttribute;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * EventDetails ddb table model
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = EventDetails.TABLE_NAME)
public class EventDetails {
    public static final String TABLE_NAME = "EventDetails";
    static final String HASH_KEY = "eventId";
    static final String RANGE_KEY = "eventTimeStamp";
    static final String CATEGORY_ATTRIBUTE = "category";
    static final String TIME_TO_LIVE_ATTRIBUTE = "ttl";
    public static final String USER_INDEX = "UserIndex";
    public static final String DOMAIN_INDEX = "DomainIndex";
    public static final String EVENT_TIME_CATEGORY_INDEX = "EventTimeCategoryIndex";

    /**
     * Hash key of the table
     */
    @DynamoDBHashKey(attributeName = HASH_KEY)
    private String eventId;

    /**
     * Range key of the table
     */
    @DynamoDBRangeKey(attributeName = RANGE_KEY)
    private Long eventTimeStamp;

    @DynamoDBIndexHashKey(attributeName = CATEGORY_ATTRIBUTE, globalSecondaryIndexName = EVENT_TIME_CATEGORY_INDEX)
    private String category;

    @DynamoDBIndexRangeKey(localSecondaryIndexName = USER_INDEX)
    private String userId;

    @DynamoDBAttribute
    private String actualEventTimeStamp;

    @DynamoDBAttribute
    private String url;

    @DynamoDBIndexRangeKey(localSecondaryIndexName = DOMAIN_INDEX)
    private String domain;

    @DynamoDBAttribute
    private String body;

    @DynamoDBAttribute(attributeName = TIME_TO_LIVE_ATTRIBUTE)
    private Long ttl;

    @DynamoDBVersionAttribute
    private Long dynamoDbVersion;
}