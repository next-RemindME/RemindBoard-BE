package com.remind.board.board.domain.dto;

import com.remind.board.board.domain.entity.Board;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardResponse {

  private Long boardId;
  private String boardName;
  private String boardOrders;
  private List<BoardCardResponse> boardCards;

  public static BoardResponse from(Board board) {
    return BoardResponse.builder()
        .boardId(board.getId())
        .boardName(board.getBoardName())
        .boardOrders(board.getBoardOrders())
        .boardCards(
            board.getBoardCards()
                .stream().map(boardCard -> BoardCardResponse.from(boardCard))
                .collect(Collectors.toList()))
        .build();
  }

}
