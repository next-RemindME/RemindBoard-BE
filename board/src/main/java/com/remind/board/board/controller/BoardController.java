package com.remind.board.board.controller;

import static com.remind.board.common.security.JwtAuthenticationFilter.TOKEN_HEADER;
import static com.remind.board.common.security.JwtAuthenticationFilter.TOKEN_PREFIX;

import com.remind.board.board.domain.dto.AddBoardForm;
import com.remind.board.board.domain.dto.AddBoardResponse;
import com.remind.board.board.domain.dto.BoardDto;
import com.remind.board.board.domain.dto.UpdateBoardResponse;
import com.remind.board.board.domain.dto.UpdateUnitBoardForm;
import com.remind.board.board.service.BoardService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/remind-board")
@RequiredArgsConstructor
public class BoardController {

  private final BoardService boardService;

  /**
   * 로그인한 회원의 board 목록 조회 (with boardCard)
   * board 엔티티의 id 값을 group 하여 boardCard 목록을 조회합니다.
   * (정렬 : boardOrders 에 맞춰 그룹별 정렬 이후, group 안에서 각각 cardOrders에 맞춰서 정렳)
   * */
  @GetMapping
  @PreAuthorize("hasAuthority('MEMBER')")
  public ResponseEntity<List<BoardDto>> showMembersBoards(@RequestHeader(value = TOKEN_HEADER) String token) {

    String refinedToken = token.substring(TOKEN_PREFIX.length());
    return ResponseEntity.ok(boardService.showMembersBoards(refinedToken));
  }

  /**
   * 로그인 여부 관계없이 공유된 board 목록 조회 (with boardCard)
   * board 엔티티의 id 값을 group 하여 boardCard 목록을 조회합니다.
   * (정렬 : board의 수정일시 최신순으로 그룹을 정렬 후, group 안에서 각각 cardOrders에 맞춰서 정렳)
   * */
  @GetMapping("/shared")
  public ResponseEntity<List<BoardDto>> showSharedBoards() {
    return ResponseEntity.ok(boardService.showSharedBoards());
  }

  /**
   * 로그인한 회원의 board 등록
   * 자식 엔티티에 해당하는 card를 동시에 여러 개 같이 등록 or board만 등록 가능
   * */
  @PostMapping
  @PreAuthorize("hasAuthority('MEMBER')")
  public ResponseEntity<AddBoardResponse> addBoard(
      @RequestHeader(value = TOKEN_HEADER) String token, @Valid @RequestBody AddBoardForm form) {

    String refinedToken = token.substring(TOKEN_PREFIX.length());
    return ResponseEntity.ok(AddBoardResponse.from(boardService.addBoard(refinedToken, form)));
  }

  /**
   * 로그인한 회원의 board 수정
   * */
  @PutMapping
  @PreAuthorize("hasAuthority('MEMBER')")
  public ResponseEntity<UpdateBoardResponse> updateBoard(
      @RequestHeader(value = TOKEN_HEADER) String token, @Valid @RequestBody UpdateUnitBoardForm form) {

    String refinedToken = token.substring(TOKEN_PREFIX.length());
    return ResponseEntity.ok(
        UpdateBoardResponse.from(boardService.updateBoard(refinedToken, form)));
  }

  /**
   * 로그인한 회원의 board 삭제
   * (Board 엔티티 - Card 엔티티 양방향 매핑 후,
   * 부모인 Board 엔티티에 Cascade 속성을 부여해주었기 때문에
   * 자식 entity인 card도 같이 delete 됩니다.)
   * */
  @DeleteMapping
  @PreAuthorize("hasAuthority('MEMBER')")
  public ResponseEntity<Void> deleteBoard(@RequestHeader(value = TOKEN_HEADER) String token,
      @RequestParam(value = "boardId", name = "boardId", required = true, defaultValue = "0") Long boardId) {

    String refinedToken = token.substring(TOKEN_PREFIX.length());
    boardService.deleteBoard(refinedToken, boardId);
    return ResponseEntity.ok().build();
  }

}
