package com.remind.board.common.exception.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BoardErrorCode {

  NOT_EXIST_BOARD(HttpStatus.BAD_REQUEST, "해당 게시 보드는 현재 찾을 수 없습니다. 다시 한번 확인해주세요."),
  NOT_EXIST_CARD(HttpStatus.BAD_REQUEST, "해당 URL CARD를 현재 찾을 수 없습니다. 다시 한번 확인해주세요."),
  NOT_ACCEPT_REWRITE(HttpStatus.BAD_REQUEST, "해당 게시 보드의 작성자만 보드를 업데이트할 수 있습니다.");

  private final HttpStatus httpStatus;
  private final String description;
}
