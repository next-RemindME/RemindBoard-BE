package com.remind.board.board.controller;

import static com.remind.board.common.security.JwtAuthenticationFilter.TOKEN_HEADER;
import static com.remind.board.common.security.JwtAuthenticationFilter.TOKEN_PREFIX;

import com.remind.board.board.domain.dto.AddBoardCardResponse;
import com.remind.board.board.domain.dto.AddUnitBoardCardForm;
import com.remind.board.board.domain.dto.UpdateBoardCardResponse;
import com.remind.board.board.domain.dto.UpdateUnitBoardCardForm;
import com.remind.board.board.service.BoardCardService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/remind-board/card")
@RequiredArgsConstructor
public class BoardCardController {

  private final BoardCardService boardCardService;

  /**
   * 로그인한 회원의 board card 등록
   * */
  @PostMapping
  @PreAuthorize("hasAuthority('MEMBER')")
  public ResponseEntity<AddBoardCardResponse> addBoardCard(
      @RequestHeader(value = TOKEN_HEADER) String token, @Valid @RequestBody AddUnitBoardCardForm form) {

    String refinedToken = token.substring(TOKEN_PREFIX.length());
    return ResponseEntity.ok(
        AddBoardCardResponse.from(boardCardService.addBoardCard(refinedToken, form)));
  }

  /**
   * 로그인한 회원의 board card 수정
   * */
  @PutMapping
  @PreAuthorize("hasAuthority('MEMBER')")
  public ResponseEntity<UpdateBoardCardResponse> updateBoardCard(
      @RequestHeader(value = TOKEN_HEADER) String token, @Valid @RequestBody UpdateUnitBoardCardForm form) {

    String refinedToken = token.substring(TOKEN_PREFIX.length());
    return ResponseEntity.ok(
        UpdateBoardCardResponse.from(boardCardService.updateBoardCard(refinedToken, form)));
  }

  /**
   * 로그인한 회원의 board card 삭제
   * */
  @DeleteMapping
  @PreAuthorize("hasAuthority('MEMBER')")
  public ResponseEntity<Void> deleteBoardCard(@RequestHeader(value = TOKEN_HEADER) String token,
      @RequestParam(value = "cardId", name = "cardId", required = true, defaultValue = "0") Long boardCardId) {

    String refinedToken = token.substring(TOKEN_PREFIX.length());
    boardCardService.deleteBoardCard(refinedToken, boardCardId);
    return ResponseEntity.ok().build();
  }

}
