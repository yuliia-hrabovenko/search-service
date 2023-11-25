package net.guzari.search.rest.mapper;

import net.guzari.search.domain.report.Report;
import net.guzari.search.openapi.model.ReportDto;
import net.guzari.search.openapi.model.ReportIdAndTitleDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReportDtoMapper {

    ReportDto toDto(Report report);

    ReportIdAndTitleDto toIdAndTitleDto(Report report);
}
