package net.guzari.search.domain.report;

import java.time.Instant;
import java.util.List;

public final class ReportDataReceiver {

    public static final String ID_1 = "b5a73500-4617-42f1-9e61-c3000641e0ea";
    public static final String ID_2 = "116b5201-43ab-4278-9a4a-b819a03b53ad";
    public static final String KEYWORDS = "Content";
    public static final String DATE = "2023-10-26T13:39:31.471102353Z";

    public static List<Report> getReports() {
        return List.of(getReport1(), getReport2());
    }

    public static Report getReport1() {
        return Report.builder()
                .id(ID_1)
                .title("Title")
                .authors(List.of("author1", "author2"))
                .target("Target")
                .content(KEYWORDS)
                .created(Instant.parse(DATE))
                .build();
    }

    public static Report getReport2() {
        return Report.builder()
                .id(ID_2)
                .title("Title 2")
                .authors(List.of("author"))
                .target("Target 2")
                .content(KEYWORDS)
                .created(Instant.parse(DATE))
                .build();
    }
}
