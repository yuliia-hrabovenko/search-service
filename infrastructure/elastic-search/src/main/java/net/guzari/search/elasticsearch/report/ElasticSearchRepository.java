package net.guzari.search.elasticsearch.report;

import lombok.RequiredArgsConstructor;
import net.guzari.search.domain.report.Report;
import net.guzari.search.domain.report.ReportDocumentRepository;
import net.guzari.search.domain.report.ReportRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ElasticSearchRepository implements ReportRepository {

    private final ReportDocumentRepository reportDocumentRepository;

    @Override
    public List<Report> reportAutocomplete(String keywords) {
        return reportDocumentRepository.reportAutocomplete(keywords);
    }

    @Override
    public List<Report> findReports(String keywords) {
        return reportDocumentRepository.findReports(keywords);
    }

    @Override
    public Optional<Report> findById(String id) {
        return reportDocumentRepository.findById(id);
    }

}
