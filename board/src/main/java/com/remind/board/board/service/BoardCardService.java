package com.remind.board.board.service;

import com.remind.board.board.domain.dto.AddUnitBoardCardForm;
import com.remind.board.board.domain.dto.UpdateUnitBoardCardForm;
import com.remind.board.board.domain.entity.BoardCard;

public interface BoardCardService {

  /* board card 단일 추가 */
  BoardCard addBoardCard(String refinedToken, AddUnitBoardCardForm form);

  /* board card 단일 update */
  BoardCard updateBoardCard(String refinedToken, UpdateUnitBoardCardForm form);
}
