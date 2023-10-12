package net.guzari.search.rest.mapper;

import net.guzari.search.domain.foo.Request;
import net.guzari.search.domain.foo.Response;
import net.guzari.search.openapi.model.FooRequestDto;
import net.guzari.search.openapi.model.FooResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FooDtoMapper {

    Request fromTdo(FooRequestDto requestDto);

    FooResponseDto toDto(Response response);
}
