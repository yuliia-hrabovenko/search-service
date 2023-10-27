package net.guzari.search.domain.report;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DefaultReportServiceTest {

    @Mock
    private ReportRepository reportRepository;
    @InjectMocks
    private DefaultReportService defaultReportService;

    @Test
    void givenKeywords_reportAutocomplete_thenReturnReportList() {
        when(reportRepository.reportAutocomplete(ReportDataReceiver.KEYWORDS))
                .thenReturn(ReportDataReceiver.getReports());

        List<Report> actual = defaultReportService.reportAutocomplete(ReportDataReceiver.KEYWORDS);

        assertThat(actual).containsExactly(ReportDataReceiver.getReport1(), ReportDataReceiver.getReport2());
    }

    @Test
    void givenKeywords_reportAutocomplete_thenReturnEmptyList() {
        List<Report> actual = defaultReportService.reportAutocomplete(ReportDataReceiver.KEYWORDS);

        assertThat(actual).isEmpty();
    }

    @Test
    void givenKeywords_findReports_thenReturnReportList() {
        when(reportRepository.findReports(ReportDataReceiver.KEYWORDS)).thenReturn(ReportDataReceiver.getReports());

        List<Report> actual = defaultReportService.findReports(ReportDataReceiver.KEYWORDS);

        assertThat(actual).containsExactly(ReportDataReceiver.getReport1(), ReportDataReceiver.getReport2());
    }

    @Test
    void givenKeywords_findReports_thenReturnEmptyList() {
        List<Report> actual = defaultReportService.findReports(ReportDataReceiver.KEYWORDS);

        assertThat(actual).isEmpty();
    }

    @Test
    void givenExistingId_findById_thenReturnReport() {
        when(reportRepository.findById(ReportDataReceiver.ID_1))
                .thenReturn(Optional.of(ReportDataReceiver.getReport1()));

        Optional<Report> actual = defaultReportService.findById(ReportDataReceiver.ID_1);

        assertThat(actual).contains(ReportDataReceiver.getReport1());
    }

    @Test
    void givenNotExistingId_findById_thenReturnEmptyResult() {
        Optional<Report> actual = defaultReportService.findById(ReportDataReceiver.ID_1);

        assertThat(actual).isEmpty();
    }
}
