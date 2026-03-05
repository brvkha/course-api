package com.practice.course_api.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.time.Instant;

@DynamoDbBean // Bắt buộc để AWS SDK hiểu đây là map với table DynamoDB
public class ActionLog {

    private String logId;       // Partition Key
    private String timestamp;   // Sort Key (Giúp query log theo thời gian dễ dàng)
    private String username;
    private String actionType;  // Ví dụ: "CREATE_COURSE", "VIEW_COURSE"
    private String details;

    public ActionLog() {}

    @DynamoDbPartitionKey
    public String getLogId() { return logId; }
    public void setLogId(String logId) { this.logId = logId; }

    @DynamoDbSortKey
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getActionType() { return actionType; }
    public void setActionType(String actionType) { this.actionType = actionType; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
}