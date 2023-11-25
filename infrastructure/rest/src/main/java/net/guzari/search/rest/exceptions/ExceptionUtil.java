package net.guzari.search.rest.exceptions;

import net.guzari.search.openapi.model.ErrorDto;

public abstract class ExceptionUtil {
    public static final ErrorDto INTERNAL_SERVER_ERROR = buildErrorDto(500, "Internal server error");
    public static final ErrorDto MISSING_PATH_VARIABLE_EXCEPTION =
            buildErrorDto(400, "path variable 'id' is mandatory");
    public static final ErrorDto VALIDATION_EXCEPTION = buildErrorDto(400, "size must be between 1 and 20");

    public static ErrorDto buildErrorDto(Integer code, String message) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setCode(code);
        errorDto.setMessage(message);
        return errorDto;
    }
}
