package com.remind.board.board.domain.dto;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddBoardCardForm {

  /* url */
  @NotBlank(message = "URL은 필수값입니다.")
  private String url;

  /* url에 대한 설명 */
  private String description;

  /* boardCard 객체 정렬 컬럼 - 문자열 */
  @NotBlank(message = "cardOrders 를 지정해주세요.")
  private String cardOrders;
}
