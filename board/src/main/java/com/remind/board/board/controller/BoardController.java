package com.remind.board.board.controller;

import static com.remind.board.common.security.JwtAuthenticationFilter.TOKEN_HEADER;
import static com.remind.board.common.security.JwtAuthenticationFilter.TOKEN_PREFIX;

import com.remind.board.board.domain.dto.AddBoardForm;
import com.remind.board.board.domain.dto.BoardResponse;
import com.remind.board.board.service.BoardService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/remind-board")
@RequiredArgsConstructor
public class BoardController {

  private final BoardService boardService;

  @PostMapping
  @PreAuthorize("hasAuthority('MEMBER')")
  public ResponseEntity<BoardResponse> addBoard(@RequestHeader(value = TOKEN_HEADER) String token, @Valid @RequestBody AddBoardForm form) {

    String refinedToken = token.substring(TOKEN_PREFIX.length());
    return ResponseEntity.ok(BoardResponse.from(boardService.addBoard(refinedToken, form)));
  }

}
