package com.br.remsoft.product.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    public static final String GENERIC_ERROR_MESSAGE
            = """
                Ocorreu um erro interno inesperado no sistema. Tente novamente e se 
                o problema persistir, entre em contato com o administrador do sistema.""";

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<Object> handleConflitBusiness(BaseException ex, WebRequest request) {

        HttpStatus status = ex.getHttpStatus();
        String detail = ex.getMessage();

        ExceptionProblem problem = ExceptionProblem.builder()
                .status(status.value())
                .userMessage(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUncaughtException(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ex.printStackTrace();
        ExceptionProblem problem = ExceptionProblem.builder()
                .title(status.getReasonPhrase())
                .status(status.value())
                .userMessage(GENERIC_ERROR_MESSAGE)
                .timestamp(LocalDateTime.now())
                .build();
        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handlcAcessDenied(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        ExceptionProblem problem = ExceptionProblem.builder()
                .title(status.getReasonPhrase())
                .status(status.value())
                .userMessage(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex,
                                                                      HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return ResponseEntity.status(status).headers(headers).build();
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        return handleValidationInternal(ex, headers, HttpStatus.resolve(status.value()), request, ex.getBindingResult());
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex,
                                                                   HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String userMessage = String.format("O recurso %s, que você tentou acessar, é inexistente.",
                ex.getRequestURL());

        ExceptionProblem problem = ExceptionProblem.builder()
                .title(HttpStatus.resolve(status.value()).getReasonPhrase())
                .status(status.value())
                .userMessage(userMessage)
                .timestamp(LocalDateTime.now())
                .build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
                                                        HttpStatusCode status, WebRequest request) {
        if (ex instanceof MethodArgumentTypeMismatchException) {
            return handleMethodArgumentTypeMismatch(
                    (MethodArgumentTypeMismatchException) ex, headers, HttpStatus.resolve(status.value()), request);
        }
        return super.handleTypeMismatch(ex, headers, status, request);
    }

    private ResponseEntity<Object> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {

        String userMessage = String.format("O parâmetro de URL '%s' recebeu o valor '%s', "
                        + "que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s.",
                ex.getName(), ex.getValue(), Objects.requireNonNull(ex.getRequiredType()).getSimpleName());

        ExceptionProblem problem = ExceptionProblem.builder()
                .title(status.getReasonPhrase())
                .status(status.value())
                .userMessage(userMessage)
                .timestamp(LocalDateTime.now())
                .build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        Throwable rootCause = ExceptionUtils.getRootCause(ex);
        if (rootCause instanceof InvalidFormatException) {
            return handleInvalidFormat((InvalidFormatException) rootCause, headers, HttpStatus.resolve(status.value()), request);
        } else if (rootCause instanceof PropertyBindingException) {
            return handlePropertyBinding((PropertyBindingException) rootCause, headers, HttpStatus.resolve(status.value()), request);
        }

        String userMessage = "O corpo da requisição está inválido. Verifique erro de sintaxe.";

        ExceptionProblem problem = ExceptionProblem.builder()
                .title(HttpStatus.resolve(status.value()).getReasonPhrase())
                .status(status.value())
                .userMessage(userMessage)
                .timestamp(LocalDateTime.now())
                .build();
        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    private ResponseEntity<Object> handlePropertyBinding(PropertyBindingException ex,
                                                         HttpHeaders headers, HttpStatus status, WebRequest request) {
        String path = joinPath(ex.getPath());

        String userMessage = String.format("A propriedade '%s' não existe. "
                + "Corrija ou remova essa propriedade e tente novamente.", path);

        ExceptionProblem problem = ExceptionProblem.builder()
                .title(status.getReasonPhrase())
                .status(status.value())
                .userMessage(userMessage)
                .timestamp(LocalDateTime.now())
                .build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }
    private ResponseEntity<Object> handleInvalidFormat(InvalidFormatException ex,
                                                       HttpHeaders headers, HttpStatus status, WebRequest request) {
        String path = joinPath(ex.getPath());

        String userMessage = String.format("""
                        A propriedade '%s' recebeu o valor '%s',
                        que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s.""",
                path, ex.getValue(), ex.getTargetType().getSimpleName());

        ExceptionProblem problem = ExceptionProblem.builder()
                .title(status.getReasonPhrase())
                .status(status.value())
                .userMessage(userMessage)
                .timestamp(LocalDateTime.now())
                .build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
                                                             HttpStatusCode status, WebRequest request) {
        if (body == null) {
            body = ExceptionProblem.builder()
                    .title(HttpStatus.resolve(status.value()).getReasonPhrase())
                    .status(status.value())
                    .userMessage(GENERIC_ERROR_MESSAGE)
                    .timestamp(LocalDateTime.now())
                    .build();
        }

        return super.handleExceptionInternal(ex, body, headers, status, request);
    }
    private ResponseEntity<Object> handleValidationInternal(Exception ex, HttpHeaders headers,
                                                            HttpStatus status, WebRequest request, BindingResult bindingResult) {

        String userMensage = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.";

        List<ExceptionProblem.Detail> problemObjects = bindingResult.getAllErrors().stream()
                .map(objectError -> {
                    String message = objectError.getDefaultMessage();

                    String name = objectError.getObjectName();

                    if (objectError instanceof FieldError) {
                        name = ((FieldError) objectError).getField();
                    }
                    ExceptionProblem.Detail invalidField = new ExceptionProblem.Detail();
                    invalidField.setField(name);
                    invalidField.setMessage(message);
                    return invalidField;
                })
                .collect(Collectors.toList());

        ExceptionProblem problem = ExceptionProblem.builder()
                .title(status.getReasonPhrase())
                .status(status.value())
                .userMessage(userMensage)
                .invalidFields(problemObjects)
                .timestamp(LocalDateTime.now())
                .build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    private String joinPath(List<JsonMappingException.Reference> references) {
        return references.stream()
                .map(JsonMappingException.Reference::getFieldName)
                .collect(Collectors.joining("."));
    }
}