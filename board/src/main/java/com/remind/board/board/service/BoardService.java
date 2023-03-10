package com.remind.board.board.service;

import com.remind.board.board.domain.dto.AddBoardForm;
import com.remind.board.board.domain.dto.BoardDto;
import com.remind.board.board.domain.entity.Board;
import java.util.List;

public interface BoardService {

  /* 로그인한 회원의 board 목록 조회 */
  List<BoardDto> showMembersBoards(String refinedToken);

  /* board 작성 */
  Board addBoard(String refinedToken, AddBoardForm form);
}
