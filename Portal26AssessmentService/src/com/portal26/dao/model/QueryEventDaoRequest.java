package com.portal26.dao.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

/**
 * QueryEvent dao request
 */
@Value
@Builder
@AllArgsConstructor
public class QueryEventDaoRequest {
    @NonNull Long startTime;
    @NonNull Long endTime;
    String userId;
    String domain;
    String category;
}