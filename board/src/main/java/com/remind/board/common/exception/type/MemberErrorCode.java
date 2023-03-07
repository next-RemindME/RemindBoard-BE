package com.remind.board.common.exception.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode {

  INVALID_LOGIN_INFO(HttpStatus.BAD_REQUEST, "로그인에 필요한 정보를 다시 확인해주세요."),
  NOT_EXISTS_MEMBER(HttpStatus.BAD_REQUEST, "해당 이메일을 가진 회원이 존재하지 않습니다."),
  ALREADY_EXIST_EMAIL(HttpStatus.BAD_REQUEST, "해당 이메일을 가진 회원이 이미 존재합니다. 다른 이메일을 입력하세요");

  private final HttpStatus httpStatus;
  private final String description;
}
