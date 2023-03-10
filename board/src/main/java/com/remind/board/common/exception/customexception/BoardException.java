package com.remind.board.common.exception.customexception;

import com.remind.board.common.exception.type.BoardErrorCode;
import com.remind.board.common.exception.type.MemberErrorCode;
import lombok.Getter;

/**
 * Board 게시물 관련 custom exception
 * */

@Getter
public class BoardException extends RuntimeException {

  private int status;
  private BoardErrorCode boardErrorCode;

  public BoardException(BoardErrorCode boardErrorCode) {
    this.status = boardErrorCode.getHttpStatus().value();
    this.boardErrorCode = boardErrorCode;
  }
}
