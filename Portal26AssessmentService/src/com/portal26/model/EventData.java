package com.portal26.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;

/**
 * Event data pojo
 */
@Data
@Value
@AllArgsConstructor
public class EventData {
    String event_timestamp;
    String user_id;
    String body;
}
