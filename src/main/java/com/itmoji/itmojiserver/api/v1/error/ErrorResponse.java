package com.itmoji.itmojiserver.api.v1.error;

import jakarta.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BindingResult;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {

  private String message;
  private int status;
  private List<FieldError> errors;
  private String code;


  private ErrorResponse(final ErrorCode code, final List<FieldError> errors) {
    this.message = code.getMessage();
    this.status = code.getStatus();
    this.errors = errors;
    this.code = code.getCode();
  }

  private ErrorResponse(final ErrorCode code) {
    this.message = code.getMessage();
    this.status = code.getStatus();
    this.code = code.getCode();
    this.errors = new ArrayList<>();
  }

  private ErrorResponse(final ErrorCode code, final Set<ConstraintViolation<?>> violations) {
    this.message = code.getMessage();
    this.status = code.getStatus();
    this.errors = violations.stream()
        .map(violation -> new FieldError(
            violation.getPropertyPath().toString(),
            violation.getInvalidValue() == null ? "" : violation.getInvalidValue().toString(),
            violation.getMessage()
        ))
        .collect(Collectors.toList());
    this.code = code.getCode();
  }

  private ErrorResponse(final ErrorCode code, final MethodParameter parameter) {
    this.message = code.getMessage();
    this.status = code.getStatus();
    this.code = code.getCode();
    this.errors = new ArrayList<>();
    this.errors.add(new FieldError(
        Objects.requireNonNull(parameter.getParameterName()),
        parameter.getParameterType().getSimpleName(),
        "Type mismatch"
    ));
  }

  public static ErrorResponse of(final ErrorCode code, final Set<ConstraintViolation<?>> violations) {
    return new ErrorResponse(code, violations);
  }


  public static ErrorResponse of(final ErrorCode code, final BindingResult bindingResult) {
    return new ErrorResponse(code, FieldError.of(bindingResult));
  }

  public static ErrorResponse of(final ErrorCode code) {
    return new ErrorResponse(code);
  }

  public static ErrorResponse of(final ErrorCode code, final List<FieldError> errors) {
    return new ErrorResponse(code, errors);
  }

  public static ErrorResponse of(final MethodArgumentTypeMismatchException e) {
    final String value = e.getValue() == null ? "" : e.getValue().toString();
    final List<FieldError> errors = FieldError.of(e.getName(), value, e.getErrorCode());
    return new ErrorResponse(ErrorCode.INVALID_TYPE_VALUE, errors);
  }

  public static ErrorResponse of(final ErrorCode code, final MethodParameter parameter) {
    return new ErrorResponse(code, parameter);
  }


  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public static class FieldError {
    private String field;
    private String value;
    private String reason;

    private FieldError(final String field, final String value, final String reason) {
      this.field = field;
      this.value = value;
      this.reason = reason;
    }

    public static List<FieldError> of(final String field, final String value, final String reason) {
      List<FieldError> fieldErrors = new ArrayList<>();
      fieldErrors.add(new FieldError(field, value, reason));
      return fieldErrors;
    }

    private static List<FieldError> of(final BindingResult bindingResult) {
      final List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();
      return fieldErrors.stream()
          .map(error -> new FieldError(
              error.getField(),
              error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
              error.getDefaultMessage()))
          .collect(Collectors.toList());
    }
  }
}
