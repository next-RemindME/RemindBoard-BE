package com.remind.board.board.domain.repository;

import com.remind.board.board.domain.dto.BoardDto;
import java.util.List;

public interface BoardRepositoryCustom {

  /* 해당 회원의 board 목록 조회 */
  List<BoardDto> showMembersBoard(Long memberId);

  /* 공유된 board 목록만 조회 */
  List<BoardDto> showSharedBoard();
}
