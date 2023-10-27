package net.guzari.search.domain.report;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DefaultReportService implements ReportService {
    private final ReportRepository reportRepository;

    public DefaultReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    @Override
    public List<Report> reportAutocomplete(String keywords) {
        return reportRepository.reportAutocomplete(keywords);
    }

    @Override
    public List<Report> findReports(String keywords) {
        return reportRepository.findReports(keywords);
    }

    @Override
    public Optional<Report> findById(String id) {
        return reportRepository.findById(id);
    }
}
