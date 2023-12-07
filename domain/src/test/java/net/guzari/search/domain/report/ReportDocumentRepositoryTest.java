package net.guzari.search.domain.report;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(classes = TestConfiguration.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportDocumentRepositoryTest {

    @Container
    private static final ElasticsearchContainer ELASTICSEARCH_CONTAINER = new ReportElasticsearchContainer();

    @Autowired
    private ReportDocumentRepository reportDocumentRepository;

    @BeforeAll
    static void setUp() {
        ELASTICSEARCH_CONTAINER.start();
    }

    @AfterAll
    static void destroy() {
        ELASTICSEARCH_CONTAINER.stop();
    }

    @Test
    void givenKeywords_reportAutocomplete_thenReturnReports() {
        reportDocumentRepository.save(ReportDataReceiver.getReport3());

        List<Report> reports = reportDocumentRepository.reportAutocomplete(ReportDataReceiver.E_MARKET);

        assertThat(reports).contains(ReportDataReceiver.getReport3());
    }

    @Test
    void givenKeywords_reportAutocomplete_thenReturnNoReports() {
        reportDocumentRepository.save(ReportDataReceiver.getReport3());

        List<Report> reports = reportDocumentRepository.reportAutocomplete(ReportDataReceiver.KEYWORDS);

        assertThat(reports).isEmpty();
    }

    @Test
    void givenKeywords_findReports_thenReturnReports() {
        reportDocumentRepository.save(ReportDataReceiver.getReport3());

        List<Report> reports = reportDocumentRepository.findReports(ReportDataReceiver.INVESTMENT);

        assertThat(reports).contains(ReportDataReceiver.getReport3());
    }

    @Test
    void givenKeywords_findReports_thenReturnNoReports() {
        reportDocumentRepository.save(ReportDataReceiver.getReport3());

        List<Report> reports = reportDocumentRepository.findReports(ReportDataReceiver.KEYWORDS);

        assertThat(reports).isEmpty();
    }

    @Test
    void givenKeywords_findById_thenReturnReport() {
        reportDocumentRepository.save(ReportDataReceiver.getReport3());

        Optional<Report> report = reportDocumentRepository.findById(ReportDataReceiver.ID_3);

        assertThat(report).contains(ReportDataReceiver.getReport3());
    }

    @Test
    void givenKeywords_findById_thenReturnNoReport() {
        Optional<Report> report = reportDocumentRepository.findById(ReportDataReceiver.ID_1);

        assertThat(report).isEmpty();
    }
}
