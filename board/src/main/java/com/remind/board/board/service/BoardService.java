package com.remind.board.board.service;

import com.remind.board.board.domain.dto.AddBoardForm;
import com.remind.board.board.domain.entity.Board;

public interface BoardService {

  /* board 작성 */
  Board addBoard(String refinedToken, AddBoardForm form);
}
