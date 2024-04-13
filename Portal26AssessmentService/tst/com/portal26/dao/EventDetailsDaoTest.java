package com.portal26.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.portal26.BaseTest;
import com.portal26.dao.ddbmodel.EventDetails;
import com.portal26.dao.model.QueryEventDaoRequest;
import com.portal26.translator.DynamoDBExceptionTranslator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class EventDetailsDaoTest extends BaseTest {
    private DynamoDBMapper localDynamoDBMapper;
    private DynamoDBExceptionTranslator dynamoDBExceptionTranslator;
    private EventDetailsDao eventDetailsDao;

    @Before
    public void setUp() {
        this.localDynamoDBMapper = localDdbSetup.getLocalDynamoDBMapper();
        this.dynamoDBExceptionTranslator = new DynamoDBExceptionTranslator();
        this.eventDetailsDao = new EventDetailsDao(localDynamoDBMapper, dynamoDBExceptionTranslator);
        this.localDdbSetup.createTables();
    }

    @Test
    public void createEventDetailsSuccess() {
        final EventDetails eventDetails = getEvenDetails(TEST_EVENT_ID1);
        final String actualOutput = eventDetailsDao.createEvent(eventDetails);
        Assert.assertEquals(TEST_EVENT_ID1, actualOutput);
        final EventDetails eventDetailsActualOutput = localDynamoDBMapper.load(EventDetails.class, TEST_EVENT_ID1,
                TEST_EVENT_TIME_STAMP1, DynamoDBMapperConfig.ConsistentReads.CONSISTENT.config());
        eventDetails.setDynamoDbVersion(1L);
        Assert.assertEquals(eventDetails, eventDetailsActualOutput);
    }

    @Test
    public void getEventDetailsEmptySuccess() {
        QueryEventDaoRequest request = QueryEventDaoRequest.builder().startTime(TEST_EVENT_TIME_STAMP2)
                .endTime(TEST_EVENT_TIME_STAMP3).userId(TEST_EVENT_USER1).domain(TEST_EVENT_DOMAIN1)
                .category(TEST_EVENT_CATEGORY1).build();
        eventDetailsDao.createEvent(getEvenDetails(TEST_EVENT_ID1, TEST_EVENT_TIME_STAMP2));
        eventDetailsDao.createEvent(getEvenDetails(TEST_EVENT_ID2, TEST_EVENT_TIME_STAMP3));
        eventDetailsDao.createEvent(getEvenDetails(TEST_EVENT_ID3, TEST_EVENT_TIME_STAMP1));
        final List<EventDetails> actualOutput = eventDetailsDao.getEvents(request);
        Assert.assertEquals(2, actualOutput.size());
    }
}
