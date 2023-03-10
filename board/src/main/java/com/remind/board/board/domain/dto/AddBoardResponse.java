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
public class AddBoardResponse {

  private Long boardId;
  private String boardName;
  private String boardOrders;
  private List<AddBoardCardResponse> boardCards;

  public static AddBoardResponse from(Board board) {
    return AddBoardResponse.builder()
        .boardId(board.getId())
        .boardName(board.getBoardName())
        .boardOrders(board.getBoardOrders())
        .boardCards(
            board.getBoardCards()
                .stream().map(boardCard -> AddBoardCardResponse.from(boardCard))
                .collect(Collectors.toList()))
        .build();
  }

}
