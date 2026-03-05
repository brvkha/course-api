package com.practice.course_api.service;

import com.practice.course_api.model.ActionLog;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActionLogService {

    private final DynamoDbEnhancedClient enhancedClient;
    private DynamoDbTable<ActionLog> actionLogTable;

    // Chạy ngay sau khi Service được khởi tạo
    @PostConstruct
    public void init() {
        // Kết nối class ActionLog với table tên là "ActionLogs"
        this.actionLogTable = enhancedClient.table("ActionLogs", TableSchema.fromBean(ActionLog.class));

        // Mẹo cho môi trường Local Docker: Tự động tạo table nếu chưa có
        try {
            actionLogTable.createTable();
            log.info("Đã khởi tạo table ActionLogs trên DynamoDB Local.");
        } catch (Exception e) {
            log.info("Table ActionLogs đã tồn tại, bỏ qua bước tạo mới.");
        }
    }

    public void logAction(String username, String actionType, String details) {
        ActionLog logEntry = new ActionLog();
        logEntry.setLogId(UUID.randomUUID().toString());
        logEntry.setTimestamp(Instant.now().toString());
        logEntry.setUsername(username);
        logEntry.setActionType(actionType);
        logEntry.setDetails(details);

        // Ghi dữ liệu xuống DynamoDB (Hoạt động cực kỳ nhanh)
        actionLogTable.putItem(logEntry);
        log.info("Đã ghi log vào DynamoDB: {} - {}", username, actionType);
    }
}