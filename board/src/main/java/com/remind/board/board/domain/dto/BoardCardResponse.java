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
public class BoardCardResponse {

  private Long boardCardId;
  private String url;
  private String description;
  private String cardOrders;

  /**
   * BoardCard 엔티티를 BoardCardResponse로 변환해주는 메서드
   * (front로 data 전달 시, 엔티티 자체를 그대로 반환하지 않고 각 컨트롤러마다 용도에 맞게 response용 dto를 반환합니다)
   * */
  public static BoardCardResponse from(BoardCard boardCard) {
    return BoardCardResponse.builder()
        .boardCardId(boardCard.getId())
        .url(boardCard.getUrl())
        .description(boardCard.getDescription())
        .cardOrders(boardCard.getCardOrders())
        .build();
  }

}
