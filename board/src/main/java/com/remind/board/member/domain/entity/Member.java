package com.remind.board.member.domain.entity;

import com.remind.board.common.entity.BaseEntity;
import com.remind.board.member.domain.dto.SignUpForm;
import com.remind.board.member.domain.dto.SignUpResponse;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.AuditOverride;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@AuditOverride(forClass = BaseEntity.class)
public class Member extends BaseEntity {

  @Id
  @Column(name="id", nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String email;

  private String nickname;

  private String password;

  public static Member of(SignUpForm form, String encodedPassword) {
    return Member.builder()
        .email(form.getEmail())
        .password(encodedPassword)
        .nickname(form.getNickname())
        .build();
  }

  public static SignUpResponse from(Member member) {
    return SignUpResponse.builder()
        .email(member.getEmail())
        .nickname(member.getNickname())
        .signUpResult(true)
        .build();
  }

}
