package net.guzari.search.domain.report;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static net.guzari.search.domain.report.CacheConstants.*;

@Service
public class DefaultReportService implements ReportService {
    private final ReportRepository reportRepository;
    private final ReportNotification reportNotification;
    private final UserContext userContext;

    public DefaultReportService(ReportRepository reportRepository, ReportNotification reportNotification,
                                UserContext userContext) {
        this.reportRepository = reportRepository;
        this.reportNotification = reportNotification;
        this.userContext = userContext;
    }

    @Override
    @CheckFeatures(Feature.AUTOCOMPLETE)
    @Cacheable(value = CACHE_AUTOCOMPLETE, key = "#keywords")
    public List<Report> reportAutocomplete(String keywords) {
        List<Report> reports = reportRepository.reportAutocomplete(keywords);
        reportNotification.notify(buildReportListResponse(reports));
        return reports;
    }

    @Override
    @CheckFeatures(Feature.SEARCH)
    @Cacheable(value = CACHE_SEARCH, key = "#keywords")
    public List<Report> findReports(String keywords) {
        List<Report> reports = reportRepository.findReports(keywords);
        reportNotification.notify(buildReportListResponse(reports));
        return reports;
    }

    @Override
    @CheckFeatures(Feature.BASE)
    @Cacheable(value = CACHE_BASE, key = "#id")
    public Optional<Report> findById(String id) {
        Optional<Report> report = reportRepository.findById(id);
        reportNotification.notify(buildReportResponse(report));
        return report;
    }

    private SqsResponse buildReportResponse(Optional<Report> report) {
        Report report2 = report.orElse(null);
        return SqsResponse.builder()
                .email(userContext.getAuthenticatedUserEmail())
                .report(report2)
                .build();
    }

    private SqsListResponse buildReportListResponse(List<Report> reports) {
        return SqsListResponse.builder()
                .email(userContext.getAuthenticatedUserEmail())
                .reports(reports)
                .build();
    }
}
