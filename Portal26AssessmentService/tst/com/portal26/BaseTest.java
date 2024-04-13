package com.portal26;

import com.portal26.dao.ddbmodel.EventDetails;

public class BaseTest {

    protected final LocalDdbSetup localDdbSetup;

    public static final String TEST_EVENT_ID1 = "uniqueEventId1";
    public static final String TEST_EVENT_ID2 = "uniqueEventId2";
    public static final String TEST_EVENT_ID3 = "uniqueEventId3";
    public static final String TEST_EVENT_USER1 = "johndoe";
    public static final String TEST_EVENT_BODY1 = "what is the capital of India?";
    public static final String TEST_EVENT_URL1 = "https://chat.openai.com/backend-api/conversation";
    public static final String TEST_EVENT_DOMAIN1 = "openai.com";
    public static final Long TEST_EVENT_TIME_STAMP1 = 1234567899L;
    public static final Long TEST_EVENT_TIME_STAMP2 = 1334567899L;
    public static final Long TEST_EVENT_TIME_STAMP3 = 1434567899L;
    public static final Long TEST_TIME_TO_LIVE = 1234567899L;
    public static final String TEST_EVENT_CATEGORY1 = "Electronics";

    public BaseTest() {
        this.localDdbSetup = new LocalDdbSetup();
    }

    protected EventDetails getEvenDetails(String eventId) {
        return EventDetails.builder().eventId(eventId).body(TEST_EVENT_BODY1).url(TEST_EVENT_URL1)
                .domain(TEST_EVENT_DOMAIN1).userId(TEST_EVENT_USER1).eventTimeStamp(TEST_EVENT_TIME_STAMP1)
                .ttl(TEST_TIME_TO_LIVE).category(TEST_EVENT_CATEGORY1).build();
    }

    protected EventDetails getEvenDetails(String eventId, Long timeStamp) {
        return EventDetails.builder().eventId(eventId).body(TEST_EVENT_BODY1).url(TEST_EVENT_URL1)
                .domain(TEST_EVENT_DOMAIN1).userId(TEST_EVENT_USER1).eventTimeStamp(timeStamp)
                .ttl(TEST_TIME_TO_LIVE).category(TEST_EVENT_CATEGORY1).build();
    }
}
