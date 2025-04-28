package net.guzari.search.sqs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.guzari.search.domain.report.ReportNotification;
import net.guzari.search.domain.report.SqsListResponse;
import net.guzari.search.domain.report.SqsResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlResponse;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class SqsReportNotification implements ReportNotification {
    private final SqsClient sqsClient;
    private final ObjectMapper objectMapper;

    @Value("${queue.name}")
    private String queueName;

    @Value("${queue.message.report-list-group-id}")
    private String reportListGroupId;

    @Value("${queue.message.report-group-id}")
    private String reportGroupId;

    @Override
    public void notify(SqsListResponse response) {
        sqsClient.sendMessage(SendMessageRequest.builder()
                .queueUrl(getQueueUrl())
                .messageBody(getMessageBody(response))
                .messageGroupId(reportListGroupId)
                .build());

        log.info("Notification: {}", response);
    }

    @Override
    public void notify(SqsResponse response) {
        sqsClient.sendMessage(SendMessageRequest.builder()
                .queueUrl(getQueueUrl())
                .messageBody(getMessageBody2(response))
                .messageGroupId(reportGroupId)
                .build());
        log.info("Notification: {}", response);
    }

    private String getQueueUrl() {
        GetQueueUrlResponse urlResponse = sqsClient.getQueueUrl(GetQueueUrlRequest.builder()
                .queueName(queueName)
                .build());
        return urlResponse.queueUrl();
    }

    private String getMessageBody(SqsListResponse response) {
        try {
            return objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing object to JSON", e);
        }
    }

    private String getMessageBody2(SqsResponse response) {
        try {
            return objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing object to JSON", e);
        }
    }
}
