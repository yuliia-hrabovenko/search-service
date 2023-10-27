package net.guzari.search.domain.report;

import java.util.List;
import java.util.Optional;

public interface ReportService {

    List<Report> reportAutocomplete(String keywords);

    List<Report> findReports(String keywords);

    Optional<Report> findById(String id);
}
