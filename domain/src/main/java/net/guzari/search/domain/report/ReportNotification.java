package net.guzari.search.domain.report;

public interface ReportNotification {

    void notify(SqsListResponse response);

    void notify(SqsResponse response);
}
