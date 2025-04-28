package net.guzari.search.sqs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.guzari.search.domain.report.Report;
import net.guzari.search.domain.report.ReportRepository;
import net.guzari.search.domain.report.SqsListResponse;
import net.guzari.search.domain.report.UserContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.CreateQueueRequest;
import software.amazon.awssdk.services.sqs.model.CreateQueueResponse;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlResponse;
import software.amazon.awssdk.services.sqs.model.QueueAttributeName;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.SQS;

@SpringBootTest(classes = {SqsTestConfiguration.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class SqsReportNotificationTest {

    private static final DockerImageName LOCALSTACK_IMAGE = DockerImageName.parse("localstack/localstack:4");
    private static final LocalStackContainer LOCAL_STACK = new LocalStackContainer(LOCALSTACK_IMAGE)
            .withServices(LocalStackContainer.Service.SQS);

    static {
        LOCAL_STACK.start();
    }

    @Value("${queue.name}")
    private String queueName;

    private String queueUrl;

    @MockBean
    private ReportRepository reportRepository;

    @MockBean
    private UserContext userContext;

    @Autowired
    private SqsReportNotification sqsReportNotification;

    @Autowired
    private SqsClient sqsClient;

    @Autowired
    private ObjectMapper objectMapper;

    @DynamicPropertySource
    static void registerDynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.cloud.aws.sqs.endpoint", () -> LOCAL_STACK.getEndpointOverride(SQS).toString());
        registry.add("spring.cloud.aws.credentials.access-key", LOCAL_STACK::getAccessKey);
        registry.add("spring.cloud.aws.credentials.secret-key", LOCAL_STACK::getSecretKey);
        registry.add("spring.cloud.aws.region.static", LOCAL_STACK::getRegion);
    }

    private void createSqsQueue() {
        CreateQueueRequest createQueueRequest = CreateQueueRequest.builder()
                .queueName(queueName)
                .attributes(Map.of(
                        QueueAttributeName.FIFO_QUEUE, "true",
                        QueueAttributeName.CONTENT_BASED_DEDUPLICATION, "true"
                ))
                .build();

        CreateQueueResponse createQueueResponse = sqsClient.createQueue(createQueueRequest);
        queueUrl = createQueueResponse.queueUrl();
    }

    @BeforeEach
    public void setUp() {
        createSqsQueue();
    }

    @Test
    void givenReports_notify_thenReturnReportList() throws JsonProcessingException {
        List<Report> reports = List.of(getReport("1"), getReport("2"));
        SqsListResponse firstMessage = getMessage("email@gmail.com", reports);
        sqsReportNotification.notify(firstMessage);
        SqsListResponse secondMessage = getMessage("email2@gmail.com", reports);
        sqsReportNotification.notify(secondMessage);

        GetQueueUrlResponse queueUrlResponse = sqsClient.getQueueUrl(GetQueueUrlRequest.builder()
                .queueName(queueName).build());
        ReceiveMessageResponse response = sqsClient.receiveMessage(ReceiveMessageRequest.builder()
                .queueUrl(queueUrlResponse.queueUrl())
                .maxNumberOfMessages(10)
                .waitTimeSeconds(10)
                .build());

        assertEquals(2, response.messages().size());
        String json = response.messages().get(0).body();
        SqsListResponse actualFirstMessage = objectMapper.readValue(json, SqsListResponse.class);
        assertEquals(firstMessage, actualFirstMessage);
    }

    private static SqsListResponse getMessage(String mail, List<Report> reports) {
        return SqsListResponse.builder()
                .email(mail)
                .reports(reports)
                .build();
    }

    private static Report getReport(String id) {
        return Report.builder()
                .id(id)
                .title("title")
                .content("content")
                .target("target")
                .authors(List.of("a1", "a2"))
                .created(Instant.parse("2025-04-03T10:15:30.00Z"))
                .build();
    }

    @AfterEach
    public void tearDown() {
        deleteAllMessagesFromSqsQueue();
    }

    private void deleteAllMessagesFromSqsQueue() {
        sqsClient.purgeQueue(request -> request.queueUrl(queueUrl));
    }

}
