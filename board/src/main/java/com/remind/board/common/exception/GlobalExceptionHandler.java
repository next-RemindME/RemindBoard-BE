package com.remind.board.common.exception;

import com.remind.board.common.exception.customexception.MemberException;
import com.remind.board.common.exception.model.ErrorResponse;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * @Valid 유효성 검사 exception handler
   * */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> methodValidExceptionHandler(HttpServletRequest request, MethodArgumentNotValidException e) {

    BindingResult bindingResult = e.getBindingResult();
    String errorMessage = bindingResult.hasErrors() ? bindingResult.getFieldError().getDefaultMessage() : "";

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(ErrorResponse.builder()
            .status(HttpStatus.BAD_REQUEST.value())
            .message(errorMessage)
            .code("NOT_CORRECT_FORMAT")
            .build());
  }

  @ExceptionHandler({MemberException.class})
  public ResponseEntity<ErrorResponse> memberExceptionHandler(HttpServletRequest request, MemberException e) {

    return ResponseEntity.status(e.getStatus())
        .body(ErrorResponse.builder()
            .code(e.getMemberErrorCode().name())
            .message(e.getMemberErrorCode().getDescription())
            .status(e.getStatus())
            .build());
  }

}
