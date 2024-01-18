package net.guzari.search.rest.exceptions;

import net.guzari.search.openapi.model.ErrorDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

import static net.guzari.search.rest.exceptions.ExceptionUtil.buildErrorDto;

@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    public static final int BAD_REQUEST = 400;
    public static final int PAYMENT_REQUIRED = 402;
    private final Logger logger = LoggerFactory.getLogger(ControllerAdvice.class);

    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDto> handleConstraintViolationException(ConstraintViolationException ex) {
        String errorMessage = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(","));
        logger.error("Error occurred: {}", ex.getMessage(), ex);

        ErrorDto errorDto = buildErrorDto(BAD_REQUEST, errorMessage);

        return new ResponseEntity<>(errorDto, HttpStatus.valueOf(BAD_REQUEST));
    }

    @Override
    @ResponseBody
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                               HttpStatus status, WebRequest request) {

        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(","));
        logger.error("Error occurred: {}", errorMessage, ex);

        ErrorDto errorDto = buildErrorDto(BAD_REQUEST, errorMessage);

        return new ResponseEntity<>(errorDto, HttpStatus.valueOf(BAD_REQUEST));
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers,
                                                               HttpStatus status, WebRequest request) {

        String errorMessage = String.format("path variable '%s' is mandatory", ex.getVariableName());
        logger.error("Error occurred: {}", errorMessage, ex);
        ErrorDto errorDto = buildErrorDto(BAD_REQUEST, errorMessage);
        return new ResponseEntity<>(errorDto, HttpStatus.valueOf(BAD_REQUEST));
    }

    @ResponseBody
    @ExceptionHandler(Throwable.class)
    ResponseEntity<ErrorDto> handleInternalServerError(HttpServletRequest request, Throwable ex) {
        logger.error("Error occurred: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(ExceptionUtil.INTERNAL_SERVER_ERROR,
                HttpStatus.valueOf(ExceptionUtil.INTERNAL_SERVER_ERROR.getCode()));
    }

    @ResponseBody
    @ExceptionHandler(AccessDeniedException.class)
    ResponseEntity<ErrorDto> handleAccessDeniedException(AccessDeniedException ex) {
        logger.error("Error occurred: {}", ex.getMessage(), ex);
        ErrorDto errorDto = buildErrorDto(PAYMENT_REQUIRED, ex.getMessage());
        return new ResponseEntity<>(errorDto, HttpStatus.valueOf(errorDto.getCode()));
    }
}
