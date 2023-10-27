package net.guzari.search.domain.report;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportRepository {

    List<Report> reportAutocomplete(String keywords);

    List<Report> findReports(String keywords);

    Optional<Report> findById(String id);
}
