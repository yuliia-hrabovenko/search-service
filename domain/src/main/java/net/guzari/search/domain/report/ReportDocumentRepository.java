package net.guzari.search.domain.report;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportDocumentRepository extends ElasticsearchRepository<Report, String> {

    @Query("{\"match_phrase_prefix\": {\"title\": { \"query\": \"?0\"}}}")
    List<Report> reportAutocomplete(String keywords);

    @Query("{\"match\": {\"content\": { \"query\": \"?0\", \"fuzziness\": 2}}}")
    List<Report> findReports(String keywords);

    Optional<Report> findById(String id);
}
