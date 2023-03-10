package com.remind.board.board.domain.entity;

import com.remind.board.board.domain.dto.AddBoardForm;
import com.remind.board.common.entity.BaseEntity;
import com.remind.board.member.domain.entity.Member;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
public class Board extends BaseEntity {

  @Id
  @Column(name="id", nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /* 이름 */
  private String boardName;

  /* board order - 문자열 */
  private String boardOrders;

  /* board 생성 회원 - 단방향 매핑 */
  @ManyToOne
  @JoinColumn(name = "member_id")
  private Member member;

  /* boardCard 참조 - 양방향매핑(부모인 board가 boardCard를 참조 + 자식인 boardCard가 부모를 참조) */
  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "board_id")
  private List<BoardCard> boardCards = new ArrayList<>();

  /* board 공유 여부 */
  private boolean shareYn;

  public static Board of(Member member, AddBoardForm form) {

    //사용자가 url card list를 입력하지 않아도 우선 부모 data인 board만 저장이 가능하도록 합니다.
    if (form.getBoardCards() == null || form.getBoardCards().isEmpty()) {
      return Board.builder()
          .member(member)
          .boardName(form.getBoardName())
          .boardOrders(form.getBoardOrders())
          .shareYn(form.isShareYn())
          .build();
    }

    //사용자가 url card list를 입력했을 시, board Card 또한 저장이 되도록 합니다.
    return Board.builder()
        .member(member)
        .boardName(form.getBoardName())
        .boardOrders(form.getBoardOrders())
        .boardCards(
            form.getBoardCards().stream().map(boardCardForm -> BoardCard.from(boardCardForm))
                .collect(Collectors.toList()))
        .shareYn(form.isShareYn())
        .build();
  }

}
