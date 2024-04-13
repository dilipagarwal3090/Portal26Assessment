package com.portal26.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Create event request pojo
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateEventRequest {
    @NonNull
    private String eventTimestamp;

    @NonNull
    private String userId;

    @NonNull
    private String url;

    @NonNull
    private String body;
}