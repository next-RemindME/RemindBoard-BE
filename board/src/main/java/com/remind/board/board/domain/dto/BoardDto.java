package com.remind.board.board.domain.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardDto {

  private Long boardId;

  /* 이름 */
  private String boardName;

  /* board order - 문자열 */
  private String boardOrders;

  /* url cards */
  /* 조회 시, 카테시안 곱 발생 시 중복된 데이터 제거를 위해 set을 사용합니다 */
  @Builder.Default
  private Set<BoardCardDto> boardCards = new LinkedHashSet<>();

  /* board 공유 여부 */
  private boolean shareYn;

  @QueryProjection
  public BoardDto(Long boardId, String boardName, String boardOrders, Set<BoardCardDto> boardCards, boolean shareYn) {
    this.boardId = boardId;
    this.boardName = boardName;
    this.boardCards = boardCards;
    this.boardOrders = boardOrders;
    this.shareYn = shareYn;
  }

  /**
   * Board의 자식 엔티티 dto
   * query DSL의 Trnasform 을 이용해 boardId를 key로 한 집합 내에
   * 조회해온 card data를 매핑시켜줄 dto입니다.
   * */
  @Getter
  @Setter
  @Builder
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  @EqualsAndHashCode(of = "boardCardId") //조회 시, 자식 entity의 id를 중복 여부 판단합니다
  public static class BoardCardDto {

    private Long boardCardId;

    /* url */
    private String url;

    /* url에 대한 설명 */
    private String description;

    /* boardCard 객체 정렬 컬럼 - 문자열 */
    private String cardOrders;

    @QueryProjection
    public BoardCardDto(Long boardCardId, String url, String description, String cardOrders) {
      this.boardCardId = boardCardId;
      this.url = url;
      this.description = description;
      this.cardOrders = cardOrders;
    }
  }

}
