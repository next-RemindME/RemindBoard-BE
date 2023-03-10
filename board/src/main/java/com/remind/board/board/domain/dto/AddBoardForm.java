package com.remind.board.board.domain.dto;

import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddBoardForm {

  /* 이름 */
  @NotBlank(message = "Board 이름은 필수값입니다.")
  private String boardName;

  /* board order - 문자열 */
  @NotBlank(message = "boardOrders 를 지정해주세요.")
  private String boardOrders;

  private List<AddBoardCardForm> boardCards;

  /* board 공유 여부 */
  private boolean shareYn;
}
