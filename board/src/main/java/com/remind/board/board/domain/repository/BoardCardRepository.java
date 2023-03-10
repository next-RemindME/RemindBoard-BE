package com.remind.board.board.domain.repository;

import com.remind.board.board.domain.entity.BoardCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardCardRepository extends JpaRepository<BoardCard, Long> {

}
