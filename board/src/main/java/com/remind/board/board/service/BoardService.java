package com.remind.board.board.service;

import com.remind.board.board.domain.dto.AddBoardForm;
import com.remind.board.board.domain.dto.BoardDto;
import com.remind.board.board.domain.dto.UpdateUnitBoardForm;
import com.remind.board.board.domain.entity.Board;
import java.util.List;

public interface BoardService {

  /* 로그인한 회원의 board 목록 조회 */
  List<BoardDto> showMembersBoards(String refinedToken);

  /* 공유된 board 목록 조회 */
  List<BoardDto> showSharedBoards();

  /* board 추가(card 포함 or card 미포함) */
  Board addBoard(String refinedToken, AddBoardForm form);

  /* board 수정 */
  Board updateBoard(String refinedToken, UpdateUnitBoardForm form);

  /* board 삭제 */
  void deleteBoard(String refinedToken, Long boardId);
}
