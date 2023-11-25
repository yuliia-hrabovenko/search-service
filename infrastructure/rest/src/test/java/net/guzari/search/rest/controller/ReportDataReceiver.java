package net.guzari.search.rest.controller;

import net.guzari.search.domain.report.Report;
import net.guzari.search.openapi.model.ReportDto;
import net.guzari.search.openapi.model.ReportIdAndTitleDto;

import java.time.Instant;
import java.util.List;

public final class ReportDataReceiver {

    public static final String ID_1 = "b5a73500-4617-42f1-9e61-c3000641e0ea";
    public static final String ID_2 = "116b5201-43ab-4278-9a4a-b819a03b53ad";
    public static final String KEYWORDS = "Content";
    public static final String LONG_KEYWORDS = "This search keywords contain more than 20 symbols";
    public static final String DATE = "2023-10-26T13:39:31.471102353Z";
    public static final String TARGET = "Target";
    public static final String TITLE = "Title";
    public static final List<String> AUTHORS = List.of("author1", "author2");

    public static List<Report> getReports() {
        return List.of(getReport1(), getReport2());
    }

    public static Report getReport1() {
        return Report.builder()
                .id(ID_1)
                .title(TITLE)
                .authors(AUTHORS)
                .target(TARGET)
                .content(KEYWORDS)
                .created(Instant.parse(DATE))
                .build();
    }

    public static Report getReport2() {
        return Report.builder()
                .id(ID_2)
                .title(TITLE)
                .authors(AUTHORS)
                .target(TARGET)
                .content(KEYWORDS)
                .created(Instant.parse(DATE))
                .build();
    }

    public static ReportIdAndTitleDto getIdAndTitleDto() {

        ReportIdAndTitleDto dto = new ReportIdAndTitleDto();
        dto.setId(ID_1);
        dto.setTitle(TITLE);

        return dto;
    }

    public static ReportIdAndTitleDto getIdAndTitleDto2() {

        ReportIdAndTitleDto dto = new ReportIdAndTitleDto();
        dto.setId(ID_2);
        dto.setTitle(TITLE);

        return dto;
    }

    public static ReportDto getReportDto() {

        ReportDto dto = new ReportDto();
        dto.setId(ID_1);
        dto.setTitle(TITLE);
        dto.setAuthors(AUTHORS);
        dto.setTarget(TARGET);
        dto.setContent(KEYWORDS);
        dto.setCreated(DATE);

        return dto;
    }

    public static ReportDto getReportDto2() {

        ReportDto dto = new ReportDto();
        dto.setId(ID_2);
        dto.setTitle(TITLE);
        dto.setAuthors(AUTHORS);
        dto.setTarget(TARGET);
        dto.setContent(KEYWORDS);
        dto.setCreated(DATE);

        return dto;
    }
}
