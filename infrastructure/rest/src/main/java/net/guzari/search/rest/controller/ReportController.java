package net.guzari.search.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.guzari.search.domain.report.Report;
import net.guzari.search.domain.report.ReportService;
import net.guzari.search.openapi.api.ReportApi;
import net.guzari.search.openapi.model.ReportDto;
import net.guzari.search.openapi.model.ReportIdAndTitleDto;
import net.guzari.search.rest.exceptions.CustomException;
import net.guzari.search.rest.mapper.ReportDtoMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static net.guzari.search.rest.exceptions.ExceptionUtil.INTERNAL_SERVER_ERROR;

@RestController
public class ReportController implements ReportApi {
    private final ReportService reportService;
    private final ReportDtoMapper mapper;
    private final ObjectMapper objectMapper;

    public ReportController(ReportService reportService, ReportDtoMapper mapper, ObjectMapper objectMapper) {
        this.reportService = reportService;
        this.mapper = mapper;
        this.objectMapper = objectMapper;
    }

    @Override
    public ResponseEntity<ReportDto> findById(String id) {
        Optional<Report> reportById = reportService.findById(id).stream().findFirst();
        ReportDto reportDto = reportById.map(mapper::toDto)
                .orElseThrow(() -> new CustomException(INTERNAL_SERVER_ERROR));

        return ResponseEntity.ok(reportDto);
    }

    @Override
    public ResponseEntity<List<ReportDto>> findReports(String keywords) {

        List<Report> reports = reportService.findReports(keywords);

        if (!reports.isEmpty() && !(reports.get(0) instanceof Report)) {
            reports = convertList(reports, Report.class, objectMapper);
        }

        List<ReportDto> dtoList = reports.stream().map(mapper::toDto).toList();
        return ResponseEntity.ok(dtoList);
    }

    @Override
    public ResponseEntity<List<ReportIdAndTitleDto>> reportAutocomplete(String keywords) {

        List<Report> reports = reportService.reportAutocomplete(keywords);

        if (!reports.isEmpty() && !(reports.get(0) instanceof Report)) {
            reports = convertList(reports, Report.class, objectMapper);
        }

        List<ReportIdAndTitleDto> dtoList = reports.stream().map(mapper::toIdAndTitleDto).toList();
        return ResponseEntity.ok(dtoList);
    }

    public <T> List<T> convertList(List<?> rawList, Class<T> targetType, ObjectMapper mapper) {
        return rawList.stream()
                .map(item -> mapper.convertValue(item, targetType))
                .collect(Collectors.toList());
    }

}
