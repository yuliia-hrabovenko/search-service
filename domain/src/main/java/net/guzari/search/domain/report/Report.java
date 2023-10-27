package net.guzari.search.domain.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Report {
    private String id;
    private String title;
    private List<String> authors;
    private String target;
    private String content;
    private Instant created;
}
