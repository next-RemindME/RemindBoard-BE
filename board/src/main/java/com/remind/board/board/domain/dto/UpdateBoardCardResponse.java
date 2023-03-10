package com.remind.board.board.domain.dto;

import com.remind.board.board.domain.entity.BoardCard;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBoardCardResponse {

  private Long boardId;
  private Long boardCardId;
  private String description;
  private String cardOrders;

  /**
   * BoardCard 엔티티를 BoardCardResponse로 변환해주는 메서드
   * (front로 data 전달 시, 엔티티 자체를 그대로 반환하지 않고 각 컨트롤러마다 용도에 맞게 response용 dto를 반환합니다)
   * */
  public static UpdateBoardCardResponse from(BoardCard boardCard) {
    return UpdateBoardCardResponse.builder()
        .boardId(boardCard.getBoard().getId())
        .boardCardId(boardCard.getId())
        .description(boardCard.getDescription())
        .cardOrders(boardCard.getCardOrders())
        .build();
  }

}
