package net.guzari.search.rest.controller;

import net.guzari.search.domain.foo.FooService;
import net.guzari.search.domain.foo.Request;
import net.guzari.search.domain.foo.Response;
import net.guzari.search.openapi.api.FooApi;
import net.guzari.search.openapi.model.FooRequestDto;
import net.guzari.search.openapi.model.FooResponseDto;
import net.guzari.search.rest.mapper.FooDtoMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FooController implements FooApi {
    private final FooService fooService;
    private final FooDtoMapper mapper;

    public FooController(FooService fooService, FooDtoMapper mapper) {
        this.fooService = fooService;
        this.mapper = mapper;
    }

    @Override
    public ResponseEntity<FooResponseDto> foo(FooRequestDto fooRequestDto) {
        Request request = mapper.fromTdo(fooRequestDto);

        Response response = fooService.foo(request);

        return ResponseEntity.ok(mapper.toDto(response));
    }
}
