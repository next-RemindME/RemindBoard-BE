package com.remind.board.board.domain.repository;

import com.remind.board.board.domain.entity.BoardCard;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestParam;

public interface BoardCardRepository extends JpaRepository<BoardCard, Long> {

  List<BoardCard> findAllByBoard_IdOrderByCardOrdersAsc(@RequestParam("board_id")Long boardId);
}
