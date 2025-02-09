package com.itmoji.itmojiserver.api.v1.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum ErrorCode {

  /*
   * 기본 10개, 예외가 많을 것 같다면 20개 단위로 코드 번호 부여
   * 1. Common : C001 ~ C030
   * 2. Session : C031 ~ C040
   */

  // Common
  INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST.value(), "C001", " 잘못된 입력값입니다."),
  METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED.value(), "C002", " 요청메서드가 허용되지 않습니다."),
  ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "C003", " Entity가 존재하지 않습니다."),
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "C004", "서버에서 오류가 발생했습니다."),
  INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST.value(), "C005", " 잘못된 타입입니다."),
  HANDLE_ACCESS_DENIED(HttpStatus.FORBIDDEN.value(), "C006", "권한이 없습니다."),
  INVALID_JSON_FORMAT(HttpStatus.BAD_REQUEST.value(), "C007", "Json형식과 맞지 않습니다."),
  INVALID_REQUEST_PARAMETER(HttpStatus.BAD_REQUEST.value(), "C008", " 요청 파라미터의 타입이 잘못되었습니다."),
  FAIL_REQUEST_PARAMETER_VALIDATION(HttpStatus.BAD_REQUEST.value(), "C009", "요청 파라미터의 유효성이 맞지 않습니다."),
  BIND_ERROR(HttpStatus.BAD_REQUEST.value(), "C010", " 바인딩 에러가 발생했습니다. 파라미터를 확인해주세요."),
  ILLEGAL_ARGUMENT(HttpStatus.BAD_REQUEST.value(), "C011", "illegalArgument error"),
  NOT_FOUND(HttpStatus.NOT_FOUND.value(), "C012", "페이지를 찾을 수 없습니다."),

  // Session
  SESSION_NOT_FOUND(HttpStatus.UNAUTHORIZED.value(), "C031", "세션이 존재하지 않거나 세션 ID가 존재하지 않습니다."),
  SESSION_INVALID__USER(HttpStatus.UNAUTHORIZED.value(), "C032","유효하지 않은 유저입니다."),
  SESSION_RESIGNED_USER(HttpStatus.UNAUTHORIZED.value(), "C033", "탈퇴한 회원입니다.");

  private final int status;
  private final String code;
  private String message;

  ErrorCode(final int status, final String code, final String message) {
    this.status = status;
    this.code = code;
    this.message = message;
  }

  public void updateIllegalArgumentExceptionMessage(final String message) {
    if (this == ILLEGAL_ARGUMENT) {
      this.message = message;
    } else {
      throw new UnsupportedOperationException("Cannot set message for this error code");
    }
  }
}
