package com.remind.board.board.domain.dto;

import com.remind.board.board.domain.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBoardResponse {

  private Long boardId;
  private String boardName;
  private String boardOrders;
  private boolean shareYn;

  /**
   * Board 엔티티를 Response DTO로 변환해주는 메서드
   * (front로 data 전달 시, 엔티티 자체를 그대로 반환하지 않고 각 컨트롤러마다 용도에 맞게 response용 dto를 반환합니다)
   * */
  public static UpdateBoardResponse from(Board board) {
    return UpdateBoardResponse.builder()
        .boardId(board.getId())
        .boardName(board.getBoardName())
        .boardOrders(board.getBoardOrders())
        .shareYn(board.isShareYn())
        .build();
  }
}
