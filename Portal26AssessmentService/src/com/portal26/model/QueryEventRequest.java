package com.portal26.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Query event request pojo
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryEventRequest {
    private String fromDate;
    private String toDate;
    private String userId;
    private String domain;
    private String category;
}
