package br.com.aurora.report.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentConversionNotSupportedException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Classe responsável por interceptar as Exceções da aplicação, tratar e retornar uma mensagem amigável no padrão.
 *
 * @see ErrorResponse
 * @see ResponseEntityExceptionHandler
 */
@Log4j2
@Component
@RestControllerAdvice
public class AdviceExceptionHandler extends ResponseEntityExceptionHandler {

    //400
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@NonNull final MethodArgumentNotValidException e,
                                                                  @NonNull final HttpHeaders headers,
                                                                  @NonNull final HttpStatus status,
                                                                  @NonNull final WebRequest request) {
        log.trace(e.getMessage(), e);
        return handleExceptionInternal(e, buildErrorResponse(e,"Ocorreu um erro de validação."),
                headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(@NonNull final BindException e,
                                                         @NonNull final HttpHeaders headers,
                                                         @NonNull final HttpStatus status,
                                                         @NonNull final WebRequest request) {
        log.trace(e.getMessage(), e);
        return handleExceptionInternal(e,
                buildErrorResponse(e,"Ocorreu um erro de vinculação."),
                headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleConversionNotSupported(@NonNull final ConversionNotSupportedException e,
                                                                  @NonNull final HttpHeaders headers,
                                                                  @NonNull final HttpStatus status,
                                                                  @NonNull final WebRequest request) {
        log.trace(e.getMessage(), e);
        return handleExceptionInternal(e,
                buildErrorResponse("Ocorreu um erro de conversão.",
                        buildErrorResponseDetail(((MethodArgumentConversionNotSupportedException) e).getName(),
                                String.valueOf(e.getValue()))),
                headers, status, request
        );
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(@NonNull final HttpMessageNotReadableException e,
                                                                  @NonNull final HttpHeaders headers,
                                                                  @NonNull final HttpStatus status,
                                                                  @NonNull final WebRequest request) {
        log.trace(e.getMessage(), e);
        final var message = "Ocorreu um erro na compreensão da mensagem.";
        if (e.getCause() instanceof final InvalidFormatException cause) {
            return handleExceptionInternal(e,
                    buildErrorResponse(message, cause.getPath().stream()
                            .map(violation -> buildErrorResponseDetail(violation.getFieldName()))
                            .toList()), headers, status, request);
        } else {
            return handleExceptionInternal(e, buildErrorResponse(message), headers, status, request);
        }
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(@NonNull final TypeMismatchException e,
                                                        @NonNull final HttpHeaders headers,
                                                        @NonNull final HttpStatus status,
                                                        @NonNull final WebRequest request) {
        log.trace(e.getMessage(), e);
        return handleExceptionInternal(e,
                buildErrorResponse("Ocorreu um erro de incompatibilidade de tipo.",
                        buildErrorResponseDetail(
                                Optional.ofNullable(e.getPropertyName()).orElse(""),
                                String.format("%s : %s", e.getValue(), e.getRequiredType()))),
                headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(@NonNull final MissingServletRequestPartException e,
                                                                     @NonNull final HttpHeaders headers,
                                                                     @NonNull final HttpStatus status,
                                                                     @NonNull final WebRequest request) {
        log.trace(e.getMessage(), e);
        return handleExceptionInternal(e,
                buildErrorResponse("Ocorreu um erro, falta parte de algum parâmetro obrigatório.",
                        buildErrorResponseDetail(e.getRequestPartName())
                ), headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(@NonNull final MissingServletRequestParameterException e,
                                                                          @NonNull final HttpHeaders headers,
                                                                          @NonNull final HttpStatus status,
                                                                          @NonNull final WebRequest request) {
        log.trace(e.getMessage(), e);
        return handleExceptionInternal(e,
                buildErrorResponse("Ocorreu um erro, falta de algum parâmetro obrigatório.",
                        buildErrorResponseDetail(e.getParameterName())
                ), headers, status, request);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ErrorResponse handleMethodArgumentTypeMismatch(@NonNull final MethodArgumentTypeMismatchException e) {
        log.trace(e.getMessage(), e);
        return buildErrorResponse("Ocorreu um erro, falta de algum parâmetro obrigatório.",
                buildErrorResponseDetail(e.getName(),
                        Optional.ofNullable(e.getRequiredType())
                                .map(Class::getComponentType)
                                .map(Class::getName)
                                .orElse("")));
    }

//    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
//    @ExceptionHandler({ConstraintViolationException.class})
//    public ErrorResponse handleConstraintViolation(@NonNull final ConstraintViolationException e) {
//        log.trace(e.getMessage(), e);
//        return buildErrorResponse("Ocorreu um erro, violação de restrição.",
//                e.getConstraintViolations().stream()
//                        .map(violation -> buildErrorResponseDetail(
//                                String.valueOf(violation.getPropertyPath()),
//                                violation.getMessage()))
//                        .toList());
//    }

    // 404
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(@NonNull final NoHandlerFoundException e,
                                                                   @NonNull final HttpHeaders headers,
                                                                   @NonNull final HttpStatus status,
                                                                   @NonNull final WebRequest request) {
        log.trace(e.getMessage(), e);
        return new ResponseEntity<>(buildErrorResponse(String.format("%s %s %s",
                "Não encontrado o método correspondente a requisição.",
                e.getHttpMethod(), e.getRequestURL())), headers, status);
    }

    // 405
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(@NonNull final HttpRequestMethodNotSupportedException e,
                                                                         @NonNull final HttpHeaders headers,
                                                                         @NonNull final HttpStatus status,
                                                                         @NonNull final WebRequest request) {
        log.trace(e.getMessage(), e);
        return new ResponseEntity<>(buildErrorResponse(
            String.format("método não permitido para essa requisição. %s, Métodos suportados: %s ",
                e.getMethod(),
                Optional.ofNullable(e.getSupportedHttpMethods())
                        .map(httpMethods -> httpMethods.stream()
                                .map(HttpMethod::toString)
                                .collect(Collectors.joining(","))).orElse(""))
                        ),
                headers, HttpStatus.METHOD_NOT_ALLOWED);
    }

    // 415
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(@NonNull final HttpMediaTypeNotSupportedException e,
                                                                     @NonNull final HttpHeaders headers,
                                                                     @NonNull final HttpStatus status,
                                                                     @NonNull final WebRequest request) {
        log.trace(e.getMessage(), e);
        return new ResponseEntity<>(buildErrorResponse(
                String.format("%s tipo de mídia HTTP não suportado, os tipos suportados são: %s",
                    e.getContentType(),
                    Optional.of(e.getSupportedMediaTypes())
                            .map(types -> types.stream()
                                    .map(MediaType::toString)
                                        .collect(Collectors.joining(","))).orElse(""))),
                headers, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    //400
    @ExceptionHandler({BusinessException.class, IllegalArgumentException.class, RestClientException.class}) //ver
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlePrecondition(final Exception e) {
        log.trace(e.getMessage(), e);
        return buildErrorResponse(e.getLocalizedMessage());
    }

    // 404
    @ExceptionHandler({EntityNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(final Exception e) {
        log.trace(e.getMessage(), e);
        return buildErrorResponse(e.getLocalizedMessage());
    }

    // 500
    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleAll(final Exception e) {
        log.fatal(e.getMessage(), e);
        return buildErrorResponse(e.getLocalizedMessage());
    }

    private ErrorResponse buildErrorResponse(
            @NonNull final BindException e,
            @NonNull final String message
    ) {
        final var error = ErrorResponse.builder().message(message);
        e
                .getBindingResult()
                .getFieldErrors()
                .forEach(err ->
                        error.detail(
                                buildErrorResponseDetail(err.getField(), err.getDefaultMessage())
                        )
                );
        e
                .getBindingResult()
                .getGlobalErrors()
                .forEach(err ->
                        error.detail(
                                buildErrorResponseDetail(err.getObjectName(), err.getDefaultMessage())
                        )
                );
        return error.build();
    }

    private ErrorResponse buildErrorResponse(@NonNull final String message) {
        return ErrorResponse.builder().message(message).build();
    }

    private ErrorResponse buildErrorResponse(@NonNull final String message,
                                             @NonNull final ErrorResponse.Detail detail) {
        return ErrorResponse.builder().message(message).detail(detail).build();
    }

    private ErrorResponse buildErrorResponse(@NonNull final String message,
                                             @NonNull List<ErrorResponse.Detail> details) {
        return ErrorResponse.builder().message(message).details(details).build();
    }

    private ErrorResponse.Detail buildErrorResponseDetail(@NonNull final String name,
                                                          @Nullable final String message) {
        return ErrorResponse.Detail.builder().name(name).message(message).build();
    }

    private ErrorResponse.Detail buildErrorResponseDetail(@NonNull final String name) {
        return ErrorResponse.Detail.builder().name(name).build();
    }
}
