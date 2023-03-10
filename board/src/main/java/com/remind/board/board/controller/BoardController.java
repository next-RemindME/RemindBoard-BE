package com.remind.board.board.controller;

import static com.remind.board.common.security.JwtAuthenticationFilter.TOKEN_HEADER;
import static com.remind.board.common.security.JwtAuthenticationFilter.TOKEN_PREFIX;

import com.remind.board.board.domain.dto.AddBoardForm;
import com.remind.board.board.domain.dto.AddBoardResponse;
import com.remind.board.board.domain.dto.BoardDto;
import com.remind.board.board.service.BoardService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
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

  @GetMapping
  @PreAuthorize("hasAuthority('MEMBER')")
  public ResponseEntity<List<BoardDto>> showMembersBoards(@RequestHeader(value = TOKEN_HEADER) String token) {

    String refinedToken = token.substring(TOKEN_PREFIX.length());
    return ResponseEntity.ok(boardService.showMembersBoards(refinedToken));
  }

  @PostMapping
  @PreAuthorize("hasAuthority('MEMBER')")
  public ResponseEntity<AddBoardResponse> addBoard(@RequestHeader(value = TOKEN_HEADER) String token, @Valid @RequestBody AddBoardForm form) {

    String refinedToken = token.substring(TOKEN_PREFIX.length());
    return ResponseEntity.ok(AddBoardResponse.from(boardService.addBoard(refinedToken, form)));
  }

}
