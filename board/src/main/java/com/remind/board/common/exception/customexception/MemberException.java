package com.remind.board.common.exception.customexception;

import com.remind.board.common.exception.type.MemberErrorCode;
import lombok.Getter;

/**
 * 회원 custom exception
 * */

@Getter
public class MemberException extends RuntimeException {

  private int status;
  private MemberErrorCode memberErrorCode;

  public MemberException(MemberErrorCode memberErrorCode) {
    this.status = memberErrorCode.getHttpStatus().value();
    this.memberErrorCode = memberErrorCode;
  }
}
