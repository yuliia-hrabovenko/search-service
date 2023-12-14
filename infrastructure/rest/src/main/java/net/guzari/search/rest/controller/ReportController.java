package net.guzari.search.rest.controller;

import net.guzari.search.domain.report.Report;
import net.guzari.search.domain.report.ReportService;
import net.guzari.search.openapi.api.ReportApi;
import net.guzari.search.openapi.model.ReportDto;
import net.guzari.search.openapi.model.ReportIdAndTitleDto;
import net.guzari.search.rest.aspect.CheckFeatures;
import net.guzari.search.rest.aspect.Feature;
import net.guzari.search.rest.exceptions.CustomException;
import net.guzari.search.rest.mapper.ReportDtoMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static net.guzari.search.rest.exceptions.ExceptionUtil.INTERNAL_SERVER_ERROR;

@RestController
public class ReportController implements ReportApi {
    private final ReportService reportService;
    private final ReportDtoMapper mapper;

    public ReportController(ReportService reportService, ReportDtoMapper mapper) {
        this.reportService = reportService;
        this.mapper = mapper;
    }

    @Override
    @CheckFeatures(Feature.BASE)
    public ResponseEntity<ReportDto> findById(String id) {
        ReportDto reportDto = reportService.findById(id).map(mapper::toDto)
                .orElseThrow(() -> new CustomException(INTERNAL_SERVER_ERROR));

        return ResponseEntity.ok(reportDto);
    }

    @Override
    @CheckFeatures(Feature.SEARCH)
    public ResponseEntity<List<ReportDto>> findReports(String keywords) {

        List<Report> reports = reportService.findReports(keywords);

        return ResponseEntity.ok(reports.stream().map(mapper::toDto).toList());
    }

    @Override
    @CheckFeatures(Feature.AUTOCOMPLETE)
    public ResponseEntity<List<ReportIdAndTitleDto>> reportAutocomplete(String keywords) {

        List<Report> reports = reportService.reportAutocomplete(keywords);

        return ResponseEntity.ok(reports.stream().map(mapper::toIdAndTitleDto).toList());
    }
}
