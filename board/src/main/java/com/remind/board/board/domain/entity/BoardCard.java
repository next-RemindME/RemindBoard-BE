package com.remind.board.board.domain.entity;

import com.remind.board.board.domain.dto.AddBoardCardForm;
import com.remind.board.board.domain.dto.AddUnitBoardCardForm;
import com.remind.board.board.domain.dto.UpdateUnitBoardCardForm;
import com.remind.board.common.entity.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.envers.AuditOverride;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@AuditOverride(forClass = BaseEntity.class)
public class BoardCard extends BaseEntity {

  @Id
  @Column(name = "id", nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /* url */
  private String url;

  /* url에 대한 설명 */
  private String description;

  /* boardCard 객체 정렬 컬럼 - 문자열 */
  private String cardOrders;

  /* boardCard 참조 - 양방향매핑(부모인 board가 boardCard를 참조 + 자식인 boardCard가 부모를 참조) */
  @ManyToOne
  @JoinColumn(name = "board_id")
  private Board board;

  /* board 등록 시, cascade 옵션에 따라 board card 여러 개 동시 등록 시 필요 entity 반환 */
  public static BoardCard from(AddBoardCardForm form) {
    return BoardCard.builder()
        .url(form.getUrl())
        .description(form.getDescription())
        .cardOrders(form.getCardOrders())
        .build();
  }

  /* board card 단일 등록에 필요한 entity 반환 */
  public static BoardCard of(Board board, AddUnitBoardCardForm form) {
    return BoardCard.builder()
        .url(form.getUrl())
        .description(form.getDescription())
        .cardOrders(form.getCardOrders())
        .board(board)
        .build();
  }

  /* board card 단일 update */
  public BoardCard updateBoardCard(Board board, UpdateUnitBoardCardForm form) {

    this.url = form.getUrl();
    this.description = form.getDescription();
    this.cardOrders = form.getCardOrders();
    this.board = board;
    return this;
  }

}
