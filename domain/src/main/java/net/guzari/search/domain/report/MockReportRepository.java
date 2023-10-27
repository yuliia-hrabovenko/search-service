package net.guzari.search.domain.report;

import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.time.Instant.now;

@Configuration
public class MockReportRepository implements ReportRepository {

    private final Map<String, List<Report>> reportMap = new HashMap<>();

    public MockReportRepository() {

        Report report1 = Report.builder()
                .id("1")
                .title("eMarket title")
                .authors(List.of("Mike", "Bob"))
                .target("eMarket")
                .content("Investment Market Report")
                .created(now())
                .build();

        Report report2 = Report.builder()
                .id("2")
                .title("Insurance US with Tramp politic")
                .authors(List.of("Jake", "David"))
                .target("insurance")
                .content("Insurance Report")
                .created(now())
                .build();

        reportMap.put("keywords", List.of(report1, report2));
        reportMap.put("id", List.of(report1));
    }

    @Override
    public List<Report> reportAutocomplete(String keywords) {

        List<Report> reports = reportMap.get(keywords);

        return Optional.ofNullable(reports).orElse(List.of());
    }

    @Override
    public List<Report> findReports(String keywords) {

        List<Report> reports = reportMap.get(keywords);

        return Optional.ofNullable(reports).orElse(List.of());
    }

    @Override
    public Optional<Report> findById(String id) {

        List<Report> reports = reportMap.get(id);

        return Optional.ofNullable(reports).flatMap(report -> reports.stream().findFirst());
    }
}
